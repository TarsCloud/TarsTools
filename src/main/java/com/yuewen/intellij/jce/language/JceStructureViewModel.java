package com.yuewen.intellij.jce.language;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.psi.PsiFile;
import com.yuewen.intellij.jce.language.psi.JceFile;
import org.jetbrains.annotations.NotNull;

public class JceStructureViewModel extends StructureViewModelBase implements
        StructureViewModel.ElementInfoProvider {
    public JceStructureViewModel(PsiFile psiFile) {
        super(psiFile, new JceStructureViewElement(psiFile));
    }

    @NotNull
    public Sorter[] getSorters() {
        return new Sorter[]{Sorter.ALPHA_SORTER};
    }


    @Override
    public boolean isAlwaysShowsPlus(StructureViewTreeElement element) {
        return false;
    }

    @Override
    public boolean isAlwaysLeaf(StructureViewTreeElement element) {
        return element instanceof JceFile;
    }
}
