package com.tencent.jceplugin.language;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.tencent.jceplugin.language.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class JceStructureViewElement implements StructureViewTreeElement, SortableTreeElement {
    private PsiElement element;

    public JceStructureViewElement(PsiElement element) {
        this.element = element;
    }

    @Override
    public Object getValue() {
        return element;
    }

    @Override
    public void navigate(boolean requestFocus) {
        if (element instanceof NavigationItem) {
            ((NavigationItem) element).navigate(requestFocus);
        }
    }

    @Override
    public boolean canNavigate() {
        return element instanceof NavigationItem &&
                ((NavigationItem) element).canNavigate();
    }

    @Override
    public boolean canNavigateToSource() {
        return element instanceof NavigationItem &&
                ((NavigationItem) element).canNavigateToSource();
    }

    @NotNull
    @Override
    public String getAlphaSortKey() {
        return Objects.requireNonNull(((PsiNamedElement) element).getName());
    }

    @NotNull
    @Override
    public ItemPresentation getPresentation() {
        return Objects.requireNonNull(((NavigationItem) element).getPresentation());
    }

    @NotNull
    @Override
    public TreeElement[] getChildren() {
        if (element instanceof JceFile) {
            JceModuleInfo[] modules = ((JceFile) element).getModuleList();
            TreeElement[] treeElements = new TreeElement[modules.length];
            int pos = 0;
            for (JceModuleInfo module : modules) {
                treeElements[pos++] = new JceStructureViewElement(module);
            }
            return treeElements;
        } else if (element instanceof JceModuleInfo) {
            Collection<JceNamedElement> members = PsiTreeUtil.findChildrenOfType(element, JceNamedElement.class);
            List<JceStructureViewElement> treeElements = new ArrayList<>(members.size());
            for (JceNamedElement member : members) {
                if (member instanceof JceConstType
                        || member instanceof JceStructType
                        || member instanceof JceInterfaceInfo
                        || member instanceof JceKeyInfo
                        || member instanceof JceEnumType)
                    treeElements.add(new JceStructureViewElement(member));
            }
            return treeElements.toArray(new JceStructureViewElement[0]);
        } else if (element instanceof JceStructType) {
            List<JceFieldInfo> fields = ((JceStructType) element).getFieldInfoList();
            TreeElement[] treeElements = new TreeElement[fields.size()];
            int pos = 0;
            for (JceFieldInfo field : fields) {
                treeElements[pos++] = new JceStructureViewElement(field);
            }
            return treeElements;
        } else if (element instanceof JceInterfaceInfo) {
            List<JceFunctionInfo> functions = ((JceInterfaceInfo) element).getFunctionInfoList();
            TreeElement[] treeElements = new TreeElement[functions.size()];
            int pos = 0;
            for (JceFunctionInfo function : functions) {
                treeElements[pos++] = new JceStructureViewElement(function);
            }
            return treeElements;
        } else if (element instanceof JceEnumType) {
            List<JceEnumMember> constants = ((JceEnumType) element).getEnumMemberList();
            TreeElement[] treeElements = new TreeElement[constants.size()];
            int pos = 0;
            for (JceEnumMember constant : constants) {
                treeElements[pos++] = new JceStructureViewElement(constant);
            }
            return treeElements;
        } else if (element instanceof JceFunctionInfo) {
            JceFunctionParamList constants = ((JceFunctionInfo) element).getFunctionParamList();
            if (constants != null) {
                List<JceFunctionParam> functionParamList = constants.getFunctionParamList();
                TreeElement[] treeElements = new TreeElement[functionParamList.size()];
                int pos = 0;
                for (JceFunctionParam constant : functionParamList) {
                    treeElements[pos++] = new JceStructureViewElement(constant);
                }
                return treeElements;
            }
        }
        return EMPTY_ARRAY;
    }
}
