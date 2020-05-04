package com.yuewen.intellij.jce.language.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.yuewen.intellij.jce.language.JceLanguage;
import com.yuewen.intellij.jce.language.JceUtil;
import com.yuewen.intellij.jce.language.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class JceCustomTypeCompletionContributor extends CompletionContributor {

    public JceCustomTypeCompletionContributor() {
        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement().inside(JceFieldType.class).withLanguage(JceLanguage.INSTANCE),
                new CompletionProvider<CompletionParameters>() {
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               @NotNull ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {
                        //提示类型，仅当还没有写module名与类型名时进行提示
                        PsiElement position = parameters.getPosition().getParent();
                        if (!(position instanceof JceFieldType)) {
                            return;
                        }
                        JceFieldType fieldType = (JceFieldType) position;
                        if (alreadyComplete(fieldType)) return;
                        //开始提示模块名
                        //只提示引用的其他文件的模块名
                        JceFile currentFile = (JceFile) fieldType.getContainingFile();
                        HashMap<String, JceFile> includedFiles = new HashMap<>();
                        JceUtil.findIncludeFiles(currentFile, includedFiles);
                        for (JceFile includeFile : includedFiles.values()) {
                            if (includeFile == currentFile) {
                                continue;
                            }
                            for (JceModuleInfo moduleInfo : includeFile.getModuleList()) {
                                resultSet.addElement(LookupElementBuilder.create(moduleInfo.getName() + "::")
                                        .withIcon(moduleInfo.getPresentation().getIcon(false))
                                        .withPsiElement(moduleInfo)
                                        .withTypeText(includeFile.getName()));
                                //顺便提示一下其他模块中的struct和enum
                                List<JceStructType> structTypeList = moduleInfo.getStructTypeList();
                                for (JceStructType jceStructType : structTypeList) {
                                    resultSet.addElement(LookupElementBuilder.create(moduleInfo.getName() + "::" + jceStructType.getName())
                                            .withIcon(jceStructType.getPresentation().getIcon(false))
                                            .withPsiElement(jceStructType)
                                            .withTypeText(includeFile.getName()));
                                }
                                List<JceEnumType> enumTypeList = moduleInfo.getEnumTypeList();
                                for (JceEnumType jceEnumType : enumTypeList) {
                                    resultSet.addElement(LookupElementBuilder.create(moduleInfo.getName() + "::" + jceEnumType.getName())
                                            .withIcon(jceEnumType.getPresentation().getIcon(false))
                                            .withPsiElement(jceEnumType)
                                            .withTypeText(includeFile.getName()));
                                }
                            }
                        }
                    }
                }
        );
    }

    public static boolean alreadyComplete(JceFieldType fieldType) {
        if (fieldType.getRefModule() != null) {
            //已经有模块提示了，不用继续提示
            return true;
        }
        if (fieldType.getBuiltInTypes() != null) {
            //有基础类型了，不提示
            return true;
        }
        if (fieldType.getReference() != null && fieldType.getReference().resolve() != null) {
            //已经解析出了引用，不继续提示
            return true;
        }
        return false;
    }
}