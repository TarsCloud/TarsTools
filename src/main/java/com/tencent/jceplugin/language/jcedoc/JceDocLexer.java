package com.tencent.jceplugin.language.jcedoc;

import com.intellij.lexer.LexerBase;
import com.intellij.lexer.MergingLexerAdapter;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.text.CharArrayUtil;
import com.tencent.jceplugin.language.jcedoc.psi.JceDocTypes;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class JceDocLexer extends MergingLexerAdapter {
    public JceDocLexer() {
        super(new AsteriskStripperLexer(new JceDocCommentLexer()),
                TokenSet.create(JceDocTypes.DOC_COMMENT_DATA, JceDocTypes.DOC_SPACE));
    }

    private static class AsteriskStripperLexer extends LexerBase {
        private final JceDocCommentLexer myFlex;
        private CharSequence myBuffer;
        private int myBufferIndex;
        private int myBufferEndOffset;
        private int myTokenEndOffset;
        private int myState;
        private IElementType myTokenType;
        private boolean myAfterLineBreak;
        private boolean myInLeadingSpace;

        AsteriskStripperLexer(final JceDocCommentLexer flex) {
            myFlex = flex;
        }

        @Override
        public final void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
            myBuffer = buffer;
            myBufferIndex = startOffset;
            myBufferEndOffset = endOffset;
            myTokenType = null;
            myTokenEndOffset = startOffset;
            myFlex.reset(myBuffer, startOffset, endOffset, initialState);
        }

        @Override
        public int getState() {
            return myState;
        }

        @NotNull
        @Override
        public CharSequence getBufferSequence() {
            return myBuffer;
        }

        @Override
        public int getBufferEnd() {
            return myBufferEndOffset;
        }

        @Override
        public final IElementType getTokenType() {
            locateToken();
            return myTokenType;
        }

        @Override
        public final int getTokenStart() {
            locateToken();
            return myBufferIndex;
        }

        @Override
        public final int getTokenEnd() {
            locateToken();
            return myTokenEndOffset;
        }

        @Override
        public final void advance() {
            locateToken();
            myTokenType = null;
        }

        protected final void locateToken() {
            if (myTokenType != null) return;
            _locateToken();

            if (myTokenType == JceDocTypes.DOC_SPACE) {
                myAfterLineBreak = CharArrayUtil.containLineBreaks(myBuffer, getTokenStart(), getTokenEnd());
            }
        }

        private void _locateToken() {
            if (myTokenEndOffset == myBufferEndOffset) {
                myTokenType = null;
                myBufferIndex = myBufferEndOffset;
                return;
            }

            myBufferIndex = myTokenEndOffset;

            if (myAfterLineBreak) {
                myAfterLineBreak = false;
                while (myTokenEndOffset < myBufferEndOffset && myBuffer.charAt(myTokenEndOffset) == '*' &&
                        (myTokenEndOffset + 1 >= myBufferEndOffset || myBuffer.charAt(myTokenEndOffset + 1) != '/')) {
                    myTokenEndOffset++;
                }

                myInLeadingSpace = true;
                if (myBufferIndex < myTokenEndOffset) {
                    myTokenType = JceDocTypes.DOC_COMMENT_LEADING_ASTRISK;
                    return;
                }
            }

            if (myInLeadingSpace) {
                myInLeadingSpace = false;
                boolean lf = false;
                while (myTokenEndOffset < myBufferEndOffset && Character.isWhitespace(myBuffer.charAt(myTokenEndOffset))) {
                    if (myBuffer.charAt(myTokenEndOffset) == '\n') lf = true;
                    myTokenEndOffset++;
                }

                final int state = myFlex.yystate();
                if (state == JceDocCommentLexer.COMMENT_DATA ||
                        myTokenEndOffset < myBufferEndOffset && (myBuffer.charAt(myTokenEndOffset) == '@' ||
                                myBuffer.charAt(myTokenEndOffset) == '{' ||
                                myBuffer.charAt(myTokenEndOffset) == '\"' ||
                                myBuffer.charAt(myTokenEndOffset) == '<')) {
                    myFlex.yybegin(JceDocCommentLexer.COMMENT_DATA_START);
                }

                if (myBufferIndex < myTokenEndOffset) {
                    myTokenType = lf ||
                            state == JceDocCommentLexer.PARAM_TAG_SPACE || state == JceDocCommentLexer.TAG_DOC_SPACE ||
                            state == JceDocCommentLexer.INLINE_TAG_NAME || state == JceDocCommentLexer.DOC_TAG_VALUE_IN_PAREN
                            ? JceDocTypes.DOC_SPACE : JceDocTypes.DOC_COMMENT_DATA;

                    return;
                }
            }

            flexLocateToken();
        }

        private void flexLocateToken() {
            try {
                myState = myFlex.yystate();
                myFlex.goTo(myBufferIndex);
                myTokenType = myFlex.advance();
                myTokenEndOffset = myFlex.getTokenEnd();
            } catch (IOException e) {
                // Can't be
            }
        }
    }
}
