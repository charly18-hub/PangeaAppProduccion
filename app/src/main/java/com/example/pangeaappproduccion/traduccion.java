package com.example.pangeaappproduccion;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.Translator;

import org.jetbrains.annotations.NotNull;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link traduccion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class traduccion extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText text;
    Button identificar;
    TextView textView10,textView11;
    //FirebaseTranslator englishGermanTranslator;
    Translator englishGermanTranslator2;

    public traduccion() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment traduccion.
     */
    // TODO: Rename and change types and number of parameters
    public static traduccion newInstance(String param1, String param2) {
        traduccion fragment = new traduccion();
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



        View view = inflater.inflate(R.layout.fragment_traduccion, container, false);



        text = (EditText)view.findViewById(R.id.textoNativo);
        textView10 = (TextView)view.findViewById(R.id.textView10);
        textView11 = (TextView)view.findViewById(R.id.textView11);







        identificar = (Button)view.findViewById(R.id.idenfiticar);

        identificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //identificaIdioma(); si sirve

               /* String string = text.getText().toString();


                TranslatorOptions options =
                        new TranslatorOptions.Builder()
                                .setSourceLanguage(TranslateLanguage.ENGLISH)
                                .setTargetLanguage(TranslateLanguage.SPANISH)
                                .build();
                englishGermanTranslator2 =
                        Translation.getClient(options);


                downloadModal(string);*/

                translate_api translate=new translate_api();
                translate.setOnTranslationCompleteListener(new translate_api.OnTranslationCompleteListener() {
                    @Override
                    public void onStartTranslation() {

                    }

                    @Override
                    public void onCompleted(String text) {

                        textView10.setText(text);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
                translate.execute(text.getText().toString(),"en","es");


            }
        });


        return view;





    }



    private void downloadModal(String input) {

        Log.i("valo1",input);



        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        englishGermanTranslator2.downloadModelIfNeeded(conditions)
                .addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                Toast.makeText(getContext(),"paso a traucir",Toast.LENGTH_LONG).show();
                                translateLanguage(input);

                            }
                        }
                ).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.i("error ",e.toString());
                    }
                }
        );

    }


    private void translateLanguage(String input) {

        Log.i("valo2",input);

        String palabra = input;

        Log.i("palabra",input);

        englishGermanTranslator2.translate(text.getText().toString())
                .addOnCompleteListener(
                        new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<String> task) {
                                textView10.setText(task.toString());
                                Toast.makeText(getActivity(),"traducido",Toast.LENGTH_LONG).show();

                                Log.i("traducido",task.toString());
                            }
                        }
                )
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Log.i("error final",e.toString());

                            }
                        }
                );


    }




    public void identificaIdioma(){

        try (FirebaseLanguageIdentification languageIdentifier = FirebaseNaturalLanguage.getInstance().getLanguageIdentification()) {
            languageIdentifier.identifyLanguage(text.getText().toString())
                    .addOnSuccessListener(
                            new OnSuccessListener<String>() {
                                @Override
                                public void onSuccess(@Nullable String languageCode) {
                                    if (languageCode != "und") {
                                        Log.i(TAG, "texto: " + text.getText().toString());

                                        Log.i(TAG, "Language: " + languageCode);


                                        FirebaseTranslatorOptions options =
                                                new FirebaseTranslatorOptions.Builder()
                                                        .setSourceLanguage(FirebaseTranslateLanguage.ES)
                                                        .setTargetLanguage(FirebaseTranslateLanguage.EN)
                                                        .build();
                                        FirebaseTranslator translator =
                                                FirebaseNaturalLanguage.getInstance().getTranslator(options);

                                        translator.translate(text.getText().toString())
                                                .addOnSuccessListener(
                                                        new OnSuccessListener<String>() {
                                                            @Override
                                                            public void onSuccess(@NonNull String translatedText) {
                                                                Log.i(TAG, "trsduccion: " + translatedText);

                                                            }
                                                        })
                                                .addOnFailureListener(
                                                        new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.i(TAG, "error: " + e);

                                                            }
                                                        });





                                    } else {

                                        Log.i(TAG, "Can't identify language.");
                                    }
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(getActivity(),"error interno",Toast.LENGTH_LONG);

                                    // Model couldn?t be loaded or other internal error.
                                    // ...
                                }
                            });
        }

    }
}