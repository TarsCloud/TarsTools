/**
 * Tencent is pleased to support the open source community by making Tars available.
 *
 * Copyright (C) 2016THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.tencent.jceplugin.language.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.tencent.jceplugin.language.JceLanguage;
import com.tencent.jceplugin.language.psi.*;
import org.jetbrains.annotations.NotNull;

public class JceCompletionContributor extends CompletionContributor {
    public static final LookupElementBuilder FIELD_REQUIRE = LookupElementBuilder.create("require").withInsertHandler(new AddSpaceInsertHandler(true)).bold();
    public static final LookupElementBuilder FIELD_OPTIONAL = LookupElementBuilder.create("optional").withInsertHandler(new AddSpaceInsertHandler(true)).bold();

    public static final LookupElementBuilder UNSIGNED_SHORT = LookupElementBuilder.create("unsigned short").withInsertHandler(new AddSpaceInsertHandler(true)).bold();
    public static final LookupElementBuilder UNSIGNED_INT = LookupElementBuilder.create("unsigned int").withInsertHandler(new AddSpaceInsertHandler(true)).bold();
    public static final LookupElementBuilder UNSIGNED_BYTE = LookupElementBuilder.create("unsigned byte").withInsertHandler(new AddSpaceInsertHandler(true)).bold();
    public static final LookupElementBuilder MAP = LookupElementBuilder.create("map<,>")
            .withInsertHandler((insertionContext, element) -> {
                int end = insertionContext.getSelectionEndOffset();
                //map<,>
                //   |指向这里
                insertionContext.getEditor().getCaretModel().moveToOffset(end - 2);
            })
            .withTypeText("map<?, ?>")
            .bold();
    public static final LookupElementBuilder VECTOR = LookupElementBuilder.create("vector<>")
            .withInsertHandler((insertionContext, element) -> {
                int end = insertionContext.getSelectionEndOffset();
                //vector<>
                //      |指向这里
                insertionContext.getEditor().getCaretModel().moveToOffset(end - 1);
            })
            .withTypeText("vector<?>")
            .bold();
    public static final LookupElementBuilder STRING = LookupElementBuilder.create("string").withInsertHandler(new AddSpaceInsertHandler(true)).bold();
    public static final LookupElementBuilder DOUBLE = LookupElementBuilder.create("double").withInsertHandler(new AddSpaceInsertHandler(true)).bold();
    public static final LookupElementBuilder FLOAT = LookupElementBuilder.create("float").withInsertHandler(new AddSpaceInsertHandler(true)).bold();
    public static final LookupElementBuilder LONG = LookupElementBuilder.create("long").withInsertHandler(new AddSpaceInsertHandler(true)).bold();
    public static final LookupElementBuilder INT = LookupElementBuilder.create("int").withInsertHandler(new AddSpaceInsertHandler(true)).bold();
    public static final LookupElementBuilder SHORT = LookupElementBuilder.create("short").withInsertHandler(new AddSpaceInsertHandler(true)).bold();
    public static final LookupElementBuilder BOOL = LookupElementBuilder.create("bool").withInsertHandler(new AddSpaceInsertHandler(true)).bold();
    public static final LookupElementBuilder BYTE = LookupElementBuilder.create("byte").withInsertHandler(new AddSpaceInsertHandler(true)).bold();
    public static final LookupElementBuilder VOID = LookupElementBuilder.create("void").withInsertHandler(new AddSpaceInsertHandler(true)).bold();
    public static final LookupElementBuilder OUT = LookupElementBuilder.create("out").withInsertHandler(new AddSpaceInsertHandler(true)).bold();
    public static final LookupElementBuilder STRUCT = LookupElementBuilder.create("struct").withInsertHandler(new AddSpaceInsertHandler(true)).bold();
    public static final LookupElementBuilder INTERFACE = LookupElementBuilder.create("interface").withInsertHandler(new AddSpaceInsertHandler(true)).bold();
    public static final LookupElementBuilder CONST = LookupElementBuilder.create("const").withInsertHandler(new AddSpaceInsertHandler(true)).bold();
    public static final LookupElementBuilder ENUM = LookupElementBuilder.create("enum").withInsertHandler(new AddSpaceInsertHandler(true)).bold();
    public static final LookupElementBuilder KEY = LookupElementBuilder.create("key").withInsertHandler(new AddSpaceInsertHandler(true)).bold();
    public static final LookupElementBuilder INCLUDE = LookupElementBuilder.create("#include")
            .withInsertHandler(new AddSpaceInsertHandler(true))
            .withTypeText("include a jce file").bold();
    public static final LookupElementBuilder MODULE = LookupElementBuilder.create("module").withInsertHandler(new AddSpaceInsertHandler(true))
            .withTypeText("define a module").bold();

    public JceCompletionContributor() {
        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement().andNot(PlatformPatterns.psiElement().inside(PsiComment.class)).withLanguage(JceLanguage.INSTANCE),
                new CompletionProvider<CompletionParameters>() {
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               @NotNull ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {
                        //提示optional/require
                        JceFieldInfo parentOfType = PsiTreeUtil.getParentOfType(parameters.getPosition(), JceFieldInfo.class);
                        if (parentOfType != null) {
                            JceFieldType jceFieldType = PsiTreeUtil.getParentOfType(parameters.getPosition(), JceFieldType.class);
                            if (jceFieldType != null) {
                                PsiElement fieldLabel = PsiTreeUtil.findSiblingBackward(jceFieldType, JceTypes.FIELD_LABEL, null);
                                if (fieldLabel == null) {
                                    //缺少optional/require
                                    resultSet.addElement(FIELD_OPTIONAL);
                                    resultSet.addElement(FIELD_REQUIRE);
                                    return;
                                }
                            }
                        }
                        //提示常规的类型（基础类型、结构体、枚举）
                        JceFieldType jceFieldType = PsiTreeUtil.getParentOfType(parameters.getPosition(), JceFieldType.class);
                        if (jceFieldType != null && jceFieldType.getRefModule() != null) {
                            //已经提示过模块了
                            return;
                        }
                        //constant常量的类型
                        JceConstType jceConstType = PsiTreeUtil.getParentOfType(parameters.getPosition(), JceConstType.class);
                        if (jceFieldType != null || (jceConstType != null && jceConstType.getBuiltInTypes() == null)) {
                            resultSet.addElement(BYTE);
                            resultSet.addElement(SHORT);
                            resultSet.addElement(BOOL);
                            resultSet.addElement(INT);
                            resultSet.addElement(LONG);
                            resultSet.addElement(FLOAT);
                            resultSet.addElement(DOUBLE);
                            resultSet.addElement(STRING);
                            resultSet.addElement(UNSIGNED_SHORT);
                            resultSet.addElement(UNSIGNED_INT);
                            resultSet.addElement(UNSIGNED_BYTE);
                            //map vector属于复合类型，const不能用
                            if (jceConstType == null) {
                                resultSet.addElement(VECTOR);
                                resultSet.addElement(MAP);
                            }
                        }
                        //如果是方法返回值还要加上void
                        if (PsiTreeUtil.getParentOfType(jceFieldType, JceReturnType.class) != null) {
                            resultSet.addElement(VOID);
                        }
                        //region 方法参数：out
                        PsiElement functionParam = PsiTreeUtil.getParentOfType(jceFieldType, JceFunctionParam.class);
                        if (functionParam != null) {
                            JceFieldTypeModifier outModifier = PsiTreeUtil.getPrevSiblingOfType(jceFieldType, JceFieldTypeModifier.class);
                            if (outModifier != null && outModifier.getFirstChild() == null) {
                                //缺少修饰符号：out
                                resultSet.addElement(OUT);
                            }
                        }
                        //endregion
                        //module里面，提示 struct interface const enum key
                        JceModuleInfo parentModule = null;
                        if (parameters.getPosition().getParent() instanceof JceModuleInfo) {
                            parentModule = (JceModuleInfo) parameters.getPosition().getParent();
                        }
                        if (parentModule != null && jceConstType == null) {
                            //并且module包含{，并且也不在const里
                            PsiElement moduleChild = parentModule.getFirstChild();
                            while (moduleChild != null && !moduleChild.getNode().getElementType().equals(JceTypes.OPEN_BLOCK)) {
                                moduleChild = moduleChild.getNextSibling();
                            }
                            if (moduleChild != null) {
                                //存在{
                                resultSet.addElement(STRUCT);
                                resultSet.addElement(INTERFACE);
                                resultSet.addElement(CONST);
                                resultSet.addElement(ENUM);
                                resultSet.addElement(KEY);
                            }
                        }
                        //文件外层，#include, module
                        PsiElement parent = parameters.getPosition().getParent();
                        while (parent instanceof PsiErrorElement) parent = parent.getParent();
                        if (parent instanceof JceFile) {
                            JceIncludeInfo prevInclude = PsiTreeUtil.getPrevSiblingOfType(parameters.getPosition(), JceIncludeInfo.class);
                            //已经有#include/module则不提示
                            //prevSibling是个error，并且其prevSibling是include_info并且其lastChild是#include
                            boolean requireIncludeFilename = prevInclude != null
                                    && parameters.getPosition().getPrevSibling() != null
                                    && parameters.getPosition().getPrevSibling().getPrevSibling() instanceof PsiErrorElement
                                    && prevInclude.getIncludeFilename() == null;
                            if (!requireIncludeFilename) {
                                //说明不在关键字#include后面
                                resultSet.addElement(INCLUDE);
                                resultSet.addElement(MODULE);
                            }
                        }
                    }
                }
        );
    }
}