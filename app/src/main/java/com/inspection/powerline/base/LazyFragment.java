package com.inspection.powerline.base;


import androidx.fragment.app.Fragment;

/**
 * fragment懒加载数据
 */
public abstract class LazyFragment extends Fragment {

    protected boolean isVisible=false;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if(hidden){
            isVisible=false;
            onInVisible();
        }else{
            isVisible=true;
            onVisiale();
        }
    }


    protected  void onVisiale(){
        lazyLoad();
    }

    protected  abstract void lazyLoad();

    protected abstract void fragmentInVisible();

    protected  void onInVisible(){
        fragmentInVisible();
    }


}
