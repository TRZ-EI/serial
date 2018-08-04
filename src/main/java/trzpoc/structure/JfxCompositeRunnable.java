package trzpoc.structure;

import java.util.ArrayList;
import java.util.Collection;

public class JfxCompositeRunnable implements Runnable {
    private Collection<RunnableFragment> fragments;

    public JfxCompositeRunnable(){
        this.fragments = new ArrayList<RunnableFragment>();
    }

    @Override
    public void run() {
        for (RunnableFragment rf: this.fragments){
            rf.executeFragment();
        }
    }
    public void add(RunnableFragment fragment){
        this.fragments.add(fragment);
    }
}
