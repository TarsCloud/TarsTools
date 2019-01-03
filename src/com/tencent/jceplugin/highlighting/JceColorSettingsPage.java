package com.tencent.jceplugin.highlighting;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import com.intellij.util.containers.ContainerUtil;
import com.tencent.jceplugin.JceIcons;
import com.tencent.jceplugin.highlighting.JceSyntaxHighlighter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

import static com.tencent.jceplugin.highlighting.JceSyntaxHighlightingColors.*;

public class JceColorSettingsPage implements ColorSettingsPage {
    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
            new AttributesDescriptor("Line comment", LINE_COMMENT),
            new AttributesDescriptor("Block comment", BLOCK_COMMENT),
            new AttributesDescriptor("Keyword", KEYWORD),
            new AttributesDescriptor("Identifier", IDENTIFIER),
            new AttributesDescriptor("String", STRING),
            new AttributesDescriptor("Number", NUMBER),
            new AttributesDescriptor("Semicolon", SEMICOLON),
            new AttributesDescriptor("Colon", COLON),
            new AttributesDescriptor("Comma", COMMA),
            new AttributesDescriptor("Dot", DOT),
            new AttributesDescriptor("Operator", OPERATOR),
            new AttributesDescriptor("Brackets", BRACKETS),
            new AttributesDescriptor("Braces", BRACES),
            new AttributesDescriptor("Parentheses", PARENTHESES),
            new AttributesDescriptor("Bad character", BAD_CHARACTER),
            new AttributesDescriptor("Type specification", TYPE_SPECIFICATION),
            new AttributesDescriptor("Type reference", TYPE_REFERENCE),
            new AttributesDescriptor("Builtin type", BUILTIN_TYPE_REFERENCE),
            new AttributesDescriptor("Builtin function", BUILTIN_FUNCTION),
            new AttributesDescriptor("Exported function", EXPORTED_FUNCTION),
            new AttributesDescriptor("Local function", LOCAL_FUNCTION),
            new AttributesDescriptor("Package exported interface", PACKAGE_EXPORTED_INTERFACE),
            new AttributesDescriptor("Package exported struct", PACKAGE_EXPORTED_STRUCT),
            new AttributesDescriptor("Package exported constant", PACKAGE_EXPORTED_CONSTANT),
            new AttributesDescriptor("Package exported variable", PACKAGE_EXPORTED_VARIABLE),
            new AttributesDescriptor("Package local interface", PACKAGE_LOCAL_INTERFACE),
            new AttributesDescriptor("Package local struct", PACKAGE_LOCAL_STRUCT),
            new AttributesDescriptor("Package local constant", PACKAGE_LOCAL_CONSTANT),
            new AttributesDescriptor("Package local variable", PACKAGE_LOCAL_VARIABLE),
            new AttributesDescriptor("Struct exported member", STRUCT_EXPORTED_MEMBER),
            new AttributesDescriptor("Struct local member", STRUCT_LOCAL_MEMBER),
            new AttributesDescriptor("Method receiver", METHOD_RECEIVER),
            new AttributesDescriptor("Function parameter", FUNCTION_PARAMETER),
            new AttributesDescriptor("Local constant", LOCAL_CONSTANT),
            new AttributesDescriptor("Local variable", LOCAL_VARIABLE),
            new AttributesDescriptor("Scope declared variable", SCOPE_VARIABLE),
            new AttributesDescriptor("Label", LABEL)
    };

    private static final Map<String, TextAttributesKey> ATTRIBUTES_KEY_MAP = ContainerUtil.newTroveMap();

    static {
        ATTRIBUTES_KEY_MAP.put("tr", TYPE_REFERENCE);
        ATTRIBUTES_KEY_MAP.put("ts", TYPE_SPECIFICATION);
        ATTRIBUTES_KEY_MAP.put("bt", BUILTIN_TYPE_REFERENCE);
        ATTRIBUTES_KEY_MAP.put("bf", BUILTIN_FUNCTION);
        ATTRIBUTES_KEY_MAP.put("kw", KEYWORD);
        ATTRIBUTES_KEY_MAP.put("ef", EXPORTED_FUNCTION);
        ATTRIBUTES_KEY_MAP.put("lf", LOCAL_FUNCTION);
        ATTRIBUTES_KEY_MAP.put("pei", PACKAGE_EXPORTED_INTERFACE);
        ATTRIBUTES_KEY_MAP.put("pes", PACKAGE_EXPORTED_STRUCT);
        ATTRIBUTES_KEY_MAP.put("pec", PACKAGE_EXPORTED_CONSTANT);
        ATTRIBUTES_KEY_MAP.put("pev", PACKAGE_EXPORTED_VARIABLE);
        ATTRIBUTES_KEY_MAP.put("pli", PACKAGE_LOCAL_INTERFACE);
        ATTRIBUTES_KEY_MAP.put("pls", PACKAGE_LOCAL_STRUCT);
        ATTRIBUTES_KEY_MAP.put("plc", PACKAGE_LOCAL_CONSTANT);
        ATTRIBUTES_KEY_MAP.put("plv", PACKAGE_LOCAL_VARIABLE);
        ATTRIBUTES_KEY_MAP.put("sem", STRUCT_EXPORTED_MEMBER);
        ATTRIBUTES_KEY_MAP.put("slm", STRUCT_LOCAL_MEMBER);
        ATTRIBUTES_KEY_MAP.put("mr", METHOD_RECEIVER);
        ATTRIBUTES_KEY_MAP.put("fp", FUNCTION_PARAMETER);
        ATTRIBUTES_KEY_MAP.put("lc", LOCAL_CONSTANT);
        ATTRIBUTES_KEY_MAP.put("lv", LOCAL_VARIABLE);
        ATTRIBUTES_KEY_MAP.put("sv", SCOPE_VARIABLE);
        ATTRIBUTES_KEY_MAP.put("ll", LABEL);
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return JceIcons.FILE;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new JceSyntaxHighlighter();
    }

    @NotNull
    @Override
    public String getDemoText() {


        return "/**" +
                " * OpenID Auth Server" +
                " */" +
                "module openid{" +
                "//授权" +
                "struct AuthReq{" +
                "    0 require string appId;" +
                "    1 require string packageName;" +
                "    2 require string devName;" +
                "     3 require string encryptString;" +
                "};" +
                "struct AuthRsp{" +
                "    0 require int ret;" +
                "     1 require string msg;" +
                "    2 optional string appId;" +
                "    3 optional string encryptString;" +
                "   4 optional string callbackUrl;" +
                "};" +
                "" +
                "interface openIdAuth{" +
                "   //授权" +
                "   int Auth(AuthReq req, out AuthRsp rsp);" +
                "};" +
                "};";
    }

    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return ATTRIBUTES_KEY_MAP;
    }

    @NotNull
    @Override
    public AttributesDescriptor[] getAttributeDescriptors() {
        return DESCRIPTORS;
    }

    @NotNull
    @Override
    public ColorDescriptor[] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Jce";
    }
}
