package com.example.pangeaappproduccion;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

/*import us.zoom.sdk.JoinMeetingOptions;
import us.zoom.sdk.JoinMeetingParams;
import us.zoom.sdk.MeetingService;
import us.zoom.sdk.ZoomSDK;
import us.zoom.sdk.ZoomSDKInitParams;
import us.zoom.sdk.ZoomSDKInitializeListener;*/

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link videollamadas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class videollamadas extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText Name,MW,MP;

    Button joing,create;

    public videollamadas() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment videollamadas.
     */
    // TODO: Rename and change types and number of parameters
    public static videollamadas newInstance(String param1, String param2) {
        videollamadas fragment = new videollamadas();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

       // zoomInit(getContext());



        View view = inflater.inflate(R.layout.fragment_videollamadas, container, false);

        Name = view.findViewById(R.id.etname);
        MW = view.findViewById(R.id.eyNumber);
        MP = view.findViewById(R.id.etPass);
        joing = view.findViewById(R.id.zoom);


        joing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String MeetingNumber =MW.getText().toString();
                String MeetingPassword = MP.getText().toString();
                String UserName = Name.getText().toString();


         /*  implementando modulos solo descomenta las partes comentadas
             if(MeetingNumber.trim().length()>0 && MeetingPassword.trim().length()>0 && UserName.trim().length()>0){
                    joingMeeting(getActivity(),MeetingNumber,MeetingPassword,UserName);
                }else{
                    Toast.makeText(getActivity(),"no existe",Toast.LENGTH_LONG).show();
                }*/
            }
        });




        return view;
    }

    /* Descomentar y probar funcionalidad

    private void  zoomInit(Context context){

        ZoomSDK sdk = ZoomSDK.getInstance();

        ZoomSDKInitParams params = new ZoomSDKInitParams();



        params.appKey="MZHQRtwDo0zCfF7puUrSnFiqubmfMPF0A6mS";
        params.appSecret="tLttzxgpHjyEzfkLyzyBOXUkPk5OVU5yDb8M";
        params.domain="zoom.us";
        params.enableLog = true;


        ZoomSDKInitializeListener listener = new ZoomSDKInitializeListener(){


            @Override
            public void onZoomSDKInitializeResult(int i, int i1) {

            }

            @Override
            public void onZoomAuthIdentityExpired() {

            }
        };

        sdk.initialize(context,listener,params);


    }

    private void joingMeeting(Context context,String meetingNumber,String meetingPassword,String username){

        MeetingService meetingService = ZoomSDK.getInstance().getMeetingService();
        JoinMeetingOptions options = new JoinMeetingOptions();
        JoinMeetingParams params = new JoinMeetingParams();
        params.displayName = username;
        params.meetingNo = meetingNumber;
        params.password = meetingPassword;

        meetingService.joinMeetingWithParams(context,params,options);
    }*/


}