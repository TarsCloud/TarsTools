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

package com.tencent.jceplugin.language.psi.impl;

import com.intellij.icons.AllIcons;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.ItemPresentationWithSeparator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.intellij.util.PlatformIcons;
import com.tencent.jceplugin.language.psi.*;
import com.tencent.jceplugin.util.Utils;
import icons.JceIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;
import java.util.Optional;

public class JcePsiImplUtil {
    @NotNull
    public static String getIncludeFileName(JceIncludeInfo includeInfo) {
        if (includeInfo.getIncludeFilename() == null) {
            return "";
        }
        return getFileName(includeInfo.getIncludeFilename());
    }

    @NotNull
    public static String getFileName(PsiElement includeInfo) {
        ASTNode identifier = includeInfo.getNode().findChildByType(JceTypes.STRING_LITERAL);
        if (identifier == null) {
            //可能打字打到一半
            identifier = includeInfo.getNode().findChildByType(JceTypes.WRONG_STRING_LITERAL);
        }
        if (identifier != null) {
            //"吧啦吧啦吧啦.jce"
            String textLiterature = identifier.getText();
            //去掉首尾"
            return Utils.strip(textLiterature, "\"");
        }
        return "";
    }

    public static String getName(JceFunctionInfo jceType) {
        return Optional.ofNullable(jceType.getIdentifier()).map(PsiElement::getText).orElse("");
    }

    public static String getName(JceModuleInfo jceType) {
        return Optional.ofNullable(jceType.getIdentifier()).map(PsiElement::getText).orElse("");
    }

    public static String getName(JceInterfaceInfo jceType) {
        return Optional.ofNullable(jceType.getIdentifier()).map(PsiElement::getText).orElse("");
    }

    public static String getName(JceEnumMember jceType) {
        return Optional.of(jceType.getIdentifier()).map(PsiElement::getText).orElse("");
    }

    public static String getName(JceEnumType jceType) {
        return Optional.ofNullable(jceType.getIdentifier()).map(PsiElement::getText).orElse("");
    }

    public static String getName(JceStructType jceType) {
        return Optional.ofNullable(jceType.getIdentifier()).map(PsiElement::getText).orElse("");
    }

    public static String getName(JceFieldInfo jceType) {
        return Optional.ofNullable(jceType.getIdentifier()).map(PsiElement::getText).orElse("");
    }

    public static String getName(JceConstType jceType) {
        return Optional.ofNullable(jceType.getIdentifier()).map(PsiElement::getText).orElse("");
    }

    public static String getName(JceFunctionParam jceType) {
        PsiElement identifier = jceType.getIdentifier();
        if (identifier == null) {
            return null;
        }
        return identifier.getText();
    }

    public static PsiElement getNameIdentifier(JceFunctionInfo element) {
        ASTNode keyNode = element.getNode().findChildByType(JceTypes.IDENTIFIER);
        if (keyNode != null) {
            return keyNode.getPsi();
        } else {
            return null;
        }
    }

    public static PsiElement getNameIdentifier(JceInterfaceInfo element) {
        ASTNode keyNode = element.getNode().findChildByType(JceTypes.IDENTIFIER);
        if (keyNode != null) {
            return keyNode.getPsi();
        } else {
            return null;
        }
    }

    public static PsiElement getNameIdentifier(JceModuleInfo element) {
        ASTNode keyNode = element.getNode().findChildByType(JceTypes.IDENTIFIER);
        if (keyNode != null) {
            return keyNode.getPsi();
        } else {
            return null;
        }
    }

    public static PsiElement getNameIdentifier(JceFunctionParam element) {
        ASTNode keyNode = element.getNode().findChildByType(JceTypes.IDENTIFIER);
        if (keyNode != null) {
            return keyNode.getPsi();
        } else {
            return null;
        }
    }

    public static PsiElement getNameIdentifier(JceFieldInfo element) {
        ASTNode keyNode = element.getNode().findChildByType(JceTypes.IDENTIFIER);
        if (keyNode != null) {
            return keyNode.getPsi();
        } else {
            return null;
        }
    }

    public static PsiElement getNameIdentifier(JceStructType element) {
        ASTNode keyNode = element.getNode().findChildByType(JceTypes.IDENTIFIER);
        if (keyNode != null) {
            return keyNode.getPsi();
        } else {
            return null;
        }
    }

    public static PsiElement getNameIdentifier(JceEnumType element) {
        ASTNode keyNode = element.getNode().findChildByType(JceTypes.IDENTIFIER);
        if (keyNode != null) {
            return keyNode.getPsi();
        } else {
            return null;
        }
    }

    public static PsiElement getNameIdentifier(JceEnumMember element) {
        ASTNode keyNode = element.getNode().findChildByType(JceTypes.IDENTIFIER);
        if (keyNode != null) {
            return keyNode.getPsi();
        } else {
            return null;
        }
    }

    public static PsiElement getNameIdentifier(JceConstType element) {
        ASTNode keyNode = element.getNode().findChildByType(JceTypes.IDENTIFIER);
        if (keyNode != null) {
            return keyNode.getPsi();
        } else {
            return null;
        }
    }

    public static PsiElement setName(JceModuleInfo element, String newName) {
        ASTNode keyNode = element.getNode().findChildByType(JceTypes.IDENTIFIER);
        if (keyNode != null) {
            JceModuleInfo property = JceElementFactory.createModule(element.getProject(), newName);
            ASTNode newKeyNode = Objects.requireNonNull(property.getIdentifier()).getNode();
            element.getNode().replaceChild(keyNode, newKeyNode);
        }
        return element;
    }

    public static PsiElement setName(JceInterfaceInfo element, String newName) {
        ASTNode keyNode = element.getNode().findChildByType(JceTypes.IDENTIFIER);
        if (keyNode != null) {
            JceInterfaceInfo property = JceElementFactory.createInterface(element.getProject(), newName);
            assert property.getNameIdentifier() != null : "create interface failed";
            ASTNode newKeyNode = property.getNameIdentifier().getNode();
            element.getNode().replaceChild(keyNode, newKeyNode);
        }
        return element;
    }

    public static PsiElement setName(JceStructType element, String newName) {
        ASTNode keyNode = element.getNode().findChildByType(JceTypes.IDENTIFIER);
        if (keyNode != null) {
            JceStructType property = JceElementFactory.createStruct(element.getProject(), newName);
            ASTNode newKeyNode = Objects.requireNonNull(property.getIdentifier()).getNode();
            element.getNode().replaceChild(keyNode, newKeyNode);
        }
        return element;
    }

    public static PsiElement setName(JceFieldInfo element, String newName) {
        ASTNode keyNode = element.getNode().findChildByType(JceTypes.IDENTIFIER);
        if (keyNode != null) {
            JceFieldInfo property = JceElementFactory.createField(element.getProject(), element, newName);
            assert property.getNameIdentifier() != null : "create field failed";
            ASTNode newKeyNode = property.getNameIdentifier().getNode();
            element.getNode().replaceChild(keyNode, newKeyNode);
        }
        return element;
    }

    public static PsiElement setName(JceEnumType element, String newName) {
        ASTNode keyNode = element.getNode().findChildByType(JceTypes.IDENTIFIER);
        if (keyNode != null) {
            JceEnumType property = JceElementFactory.createEnum(element.getProject(), element, newName);
            assert property.getNameIdentifier() != null : "create enum failed";
            ASTNode newKeyNode = property.getNameIdentifier().getNode();
            element.getNode().replaceChild(keyNode, newKeyNode);
        }
        return element;
    }

    public static PsiElement setName(JceEnumMember element, String newName) {
        ASTNode keyNode = element.getNode().findChildByType(JceTypes.IDENTIFIER);
        if (keyNode != null) {
            JceEnumMember property = JceElementFactory.createEnumMember(element.getProject(), element, newName);
            assert property.getNameIdentifier() != null : "create enum member failed";
            ASTNode newKeyNode = property.getNameIdentifier().getNode();
            element.getNode().replaceChild(keyNode, newKeyNode);
        }
        return element;
    }

    public static PsiElement setName(JceConstType element, String newName) {
        ASTNode keyNode = element.getNode().findChildByType(JceTypes.IDENTIFIER);
        if (keyNode != null) {
            JceConstType property = JceElementFactory.createConst(element.getProject(), element, newName);
            assert property.getNameIdentifier() != null : "create const failed";
            ASTNode newKeyNode = property.getNameIdentifier().getNode();
            element.getNode().replaceChild(keyNode, newKeyNode);
        }
        return element;
    }

    public static PsiElement setName(JceFunctionInfo element, String newName) {
        ASTNode keyNode = element.getNode().findChildByType(JceTypes.IDENTIFIER);
        if (keyNode != null) {
            JceFunctionInfo property = JceElementFactory.createFunction(element.getProject(), element, newName);
            assert property.getIdentifier() != null : "create jce function failed";
            element.getNode().replaceChild(keyNode, property.getIdentifier().getNode());
        }
        return element;
    }

    public static PsiElement setName(JceFunctionParam element, String newName) {
        ASTNode keyNode = element.getNode().findChildByType(JceTypes.IDENTIFIER);
        if (keyNode != null) {
            JceFunctionParam property = JceElementFactory.createFunctionParam(element.getProject(), element, newName);
            assert property.getIdentifier() != null : "create function parameter failed";
            element.getNode().replaceChild(keyNode, property.getIdentifier().getNode());
        }
        return element;
    }

    public static JceIncludeFilename setName(JceIncludeFilename element, String newName) {
        ASTNode keyNode = element.getStringLiteral().getNode();
        if (keyNode != null) {
            JceIncludeInfo property = JceElementFactory.createIncludeFilename(element.getProject(), newName);
            ASTNode newKeyNode = Objects.requireNonNull(property.getIncludeFilename()).getStringLiteral().getNode();
            element.getNode().replaceChild(keyNode, newKeyNode);
        }
        return element;
    }

    public static JceFieldType setName(JceFieldType element, String newName) {
        if (element.getIdentifier() == null) {
            return element;
        }
        ASTNode keyNode = element.getIdentifier().getNode();
        if (keyNode != null) {
            JceStructType property = JceElementFactory.createStruct(element.getProject(), newName);
            ASTNode newKeyNode = Objects.requireNonNull(property.getIdentifier()).getNode();
            element.getNode().replaceChild(keyNode, newKeyNode);
        }
        return element;
    }

    public static JceFieldType setQualifierName(JceFieldType element, String newName) {
        if (element.getRefModule() == null) {
            return element;
        }
        ASTNode keyNode = element.getRefModule().getIdentifier().getNode();
        if (keyNode != null) {
            JceModuleInfo property = JceElementFactory.createModule(element.getProject(), newName);
            ASTNode newKeyNode = Objects.requireNonNull(property.getIdentifier()).getNode();
            element.getNode().replaceChild(keyNode, newKeyNode);
        }
        return element;
    }

    public static JceRefModule setName(JceRefModule element, String newName) {
        ASTNode keyNode = element.getIdentifier().getNode();
        if (keyNode != null) {
            JceModuleInfo property = JceElementFactory.createModule(element.getProject(), newName);
            ASTNode newKeyNode = Objects.requireNonNull(property.getIdentifier()).getNode();
            element.getNode().replaceChild(keyNode, newKeyNode);
        }
        return element;
    }

    @NotNull
    public static PsiReference[] getReferences(JceFieldType fieldType) {
        return ReferenceProvidersRegistry.getReferencesFromProviders(fieldType);
    }

    @Nullable
    public static PsiReference getReference(JceFieldType fieldType) {
        PsiReference[] references = getReferences(fieldType);
        return references.length == 0 ? null : references[0];
    }

    @NotNull
    public static PsiReference[] getReferences(JceFieldDefaultAssignment fieldType) {
        return ReferenceProvidersRegistry.getReferencesFromProviders(fieldType);
    }

    @Nullable
    public static PsiReference getReference(JceFieldDefaultAssignment fieldType) {
        PsiReference[] references = getReferences(fieldType);
        return references.length == 0 ? null : references[0];
    }

    @NotNull
    public static PsiReference[] getReferences(JceIncludeInfo fieldType) {
        return ReferenceProvidersRegistry.getReferencesFromProviders(fieldType);
    }

    @Nullable
    public static PsiReference getReference(JceIncludeInfo fieldType) {
        PsiReference[] references = getReferences(fieldType);
        return references.length == 0 ? null : references[0];
    }

    @NotNull
    public static PsiReference[] getReferences(JceIncludeFilename fieldType) {
        return ReferenceProvidersRegistry.getReferencesFromProviders(fieldType);
    }

    @Nullable
    public static PsiReference getReference(JceIncludeFilename fieldType) {
        PsiReference[] references = getReferences(fieldType);
        return references.length == 0 ? null : references[references.length - 1];
    }

    @NotNull
    public static PsiReference[] getReferences(JceRefModule fieldType) {
        return ReferenceProvidersRegistry.getReferencesFromProviders(fieldType);
    }

    @Nullable
    public static PsiReference getReference(JceRefModule fieldType) {
        PsiReference[] references = getReferences(fieldType);
        return references.length == 0 ? null : references[0];
    }

    @NotNull
    public static PsiReference[] getReferences(JceRefStructField jceKeyInfo) {
        return ReferenceProvidersRegistry.getReferencesFromProviders(jceKeyInfo);
    }

    @Nullable
    public static PsiReference getReference(JceRefStructField fieldType) {
        PsiReference[] references = getReferences(fieldType);
        return references.length == 0 ? null : references[0];
    }

    @NotNull
    public static PsiReference[] getReferences(JceRefStruct refStruct) {
        return ReferenceProvidersRegistry.getReferencesFromProviders(refStruct);
    }

    @Nullable
    public static PsiReference getReference(JceRefStruct refStruct) {
        PsiReference[] references = getReferences(refStruct);
        return references.length == 0 ? null : references[0];
    }

    @NotNull
    public static ItemPresentation getPresentation(JceModuleInfoImpl moduleInfo) {
        return new ItemPresentationWithSeparator() {
            @Nullable
            @Override
            public String getPresentableText() {
                return moduleInfo.getName();
            }

            @NotNull
            @Override
            public String getLocationString() {
                return moduleInfo.getContainingFile().getName();
            }

            @NotNull
            @Override
            public Icon getIcon(boolean b) {
                return JceIcons.MODULE;
            }
        };
    }

    @NotNull
    public static ItemPresentation getPresentation(JceEnumType moduleInfo) {
        return new ItemPresentationWithSeparator() {
            @Nullable
            @Override
            public String getPresentableText() {
                return moduleInfo.getName();
            }

            @NotNull
            @Override
            public String getLocationString() {
                return moduleInfo.getContainingFile().getName();
            }

            @NotNull
            @Override
            public Icon getIcon(boolean b) {
                return PlatformIcons.ENUM_ICON;
            }
        };
    }

    @NotNull
    public static ItemPresentation getPresentation(JceEnumMember moduleInfo) {
        return new ItemPresentationWithSeparator() {
            @Nullable
            @Override
            public String getPresentableText() {
                return moduleInfo.getName();
            }

            @NotNull
            @Override
            public String getLocationString() {
                return moduleInfo.getContainingFile().getName();
            }

            @NotNull
            @Override
            public Icon getIcon(boolean b) {
                return PlatformIcons.ENUM_ICON;
            }
        };
    }

    @NotNull
    public static ItemPresentation getPresentation(JceFunctionInfo moduleInfo) {
        return new ItemPresentationWithSeparator() {
            @Nullable
            @Override
            public String getPresentableText() {
                return moduleInfo.getName();
            }

            @NotNull
            @Override
            public String getLocationString() {
                return moduleInfo.getContainingFile().getName();
            }

            @NotNull
            @Override
            public Icon getIcon(boolean b) {
                return PlatformIcons.FUNCTION_ICON;
            }
        };
    }

    @NotNull
    public static ItemPresentation getPresentation(JceFunctionParam moduleInfo) {
        return new ItemPresentationWithSeparator() {
            @Nullable
            @Override
            public String getPresentableText() {
                return moduleInfo.getName();
            }

            @NotNull
            @Override
            public String getLocationString() {
                return moduleInfo.getContainingFile().getName();
            }

            @NotNull
            @Override
            public Icon getIcon(boolean b) {
                return PlatformIcons.PARAMETER_ICON;
            }
        };
    }

    @NotNull
    public static ItemPresentation getPresentation(JceFieldInfo moduleInfo) {
        return new ItemPresentationWithSeparator() {
            @Nullable
            @Override
            public String getPresentableText() {
                return moduleInfo.getName();
            }

            @NotNull
            @Override
            public String getLocationString() {
                return moduleInfo.getContainingFile().getName();
            }

            @NotNull
            @Override
            public Icon getIcon(boolean b) {
                return PlatformIcons.FIELD_ICON;
            }
        };
    }

    @NotNull
    public static ItemPresentation getPresentation(JceInterfaceInfo moduleInfo) {
        return new ItemPresentationWithSeparator() {
            @Nullable
            @Override
            public String getPresentableText() {
                return moduleInfo.getName();
            }

            @NotNull
            @Override
            public String getLocationString() {
                return moduleInfo.getContainingFile().getName();
            }

            @NotNull
            @Override
            public Icon getIcon(boolean b) {
                return PlatformIcons.INTERFACE_ICON;
            }
        };
    }

    @NotNull
    public static ItemPresentation getPresentation(JceIncludeInfo moduleInfo) {
        return new ItemPresentationWithSeparator() {
            @NotNull
            @Override
            public String getPresentableText() {
                return moduleInfo.getIncludeFileName();
            }

            @NotNull
            @Override
            public String getLocationString() {
                return moduleInfo.getContainingFile().getName();
            }

            @NotNull
            @Override
            public Icon getIcon(boolean b) {
                return JceIcons.FILE;
            }
        };
    }

    @NotNull
    public static ItemPresentation getPresentation(JceStructType moduleInfo) {
        return new ItemPresentationWithSeparator() {
            @Nullable
            @Override
            public String getPresentableText() {
                return moduleInfo.getName();
            }

            @NotNull
            @Override
            public String getLocationString() {
                return moduleInfo.getContainingFile().getName();
            }

            @NotNull
            @Override
            public Icon getIcon(boolean b) {
                return JceIcons.STRUCT;
            }
        };
    }

    @NotNull
    public static ItemPresentation getPresentation(JceConstType moduleInfo) {
        return new ItemPresentationWithSeparator() {
            @Nullable
            @Override
            public String getPresentableText() {
                return moduleInfo.getName();
            }

            @NotNull
            @Override
            public String getLocationString() {
                return moduleInfo.getContainingFile().getName();
            }

            @NotNull
            @Override
            public Icon getIcon(boolean b) {
                return AllIcons.Nodes.Constant;
            }
        };
    }
}
