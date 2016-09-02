package com.dyx.bestnews.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {

    protected OnFragmentInteractionListener mListener;
    protected View rootView;

    public BaseFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), null);

        }

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
//            Toast.makeText(BaseFragment.this.getContext(), "移除rootView", Toast.LENGTH_SHORT).show();
            parent.removeView(rootView);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initData();
//        if (rootView.getParent() != null) {
//            Toast.makeText(BaseFragment.this.getContext(), "parent:" + rootView.getParent(), Toast.LENGTH_SHORT).show();
//        }
    }

    //初始化界面
    protected abstract void initData();

    //设置显示的布局
    public abstract int getLayoutId();


    public void onButtonPressed(int viewId, Bundle bundle) {
        if (mListener != null) {
            mListener.onFragmentInteraction(viewId, bundle);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int viewId, Bundle bundle);
    }
}
