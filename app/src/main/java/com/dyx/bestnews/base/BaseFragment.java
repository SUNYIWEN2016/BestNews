package com.dyx.bestnews.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dyx.bestnews.views.LoadingPage;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {

    protected OnFragmentInteractionListener mListener;
    protected View rootView;
    private LoadingPage loadingPage;

    public BaseFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {

            loadingPage = new LoadingPage(getContext()) {
                @Override
                protected void parseData(String result) {
                    parseRealData(result);
                }

                @Override
                protected String getUrl() {
                    return getRealURL();
                }

                @Override
                protected void bindView(View sucessView) {
                    ButterKnife.bind(BaseFragment.this, sucessView);
                    initData();
                }

                @Override
                protected int getScucessLayout() {
                    return getRealLayout();
                }
            };
            rootView = loadingPage;
        }

        return rootView;
    }
    //初始化界面
    protected abstract void initData();
    protected abstract String getRealURL();
    protected abstract void parseRealData(String result);
    protected abstract int getRealLayout();

    public void showSuccessPage() {
        loadingPage.startNetWork();
    }

    ;


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
//            Toast.makeText(BaseFragment.this.getContext(), "移除rootView", Toast.LENGTH_SHORT).show();
            parent.removeView(rootView);
        }
    }

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
