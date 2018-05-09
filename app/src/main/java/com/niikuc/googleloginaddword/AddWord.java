package com.niikuc.googleloginaddword;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AddWord extends AppCompatActivity {

    Button button,bt_save,bt_search,bt_token;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    DatabaseReference dbWord,dbReference;
    EditText et_word,et_description,et_search;
    ImageView img_user;
    TextView tv_name;
    String name;
    Uri photoUrl;

    private static final String TAG="AddWord";


    RecyclerView mResultList;


    ListView list;
    ArrayList<MacWord> zborovi;
    ArrayAdapter<MacWord> wordAdapter;


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);

        button=(Button) findViewById(R.id.logOut);
        bt_save=(Button)findViewById(R.id.bt_save);
        et_word=(EditText)findViewById(R.id.et_word);
        et_description=(EditText)findViewById(R.id.et_descripion);
        tv_name=(TextView)findViewById(R.id.textView);
        user = FirebaseAuth.getInstance().getCurrentUser();
        img_user=(ImageView)findViewById(R.id.imageView);


        et_search=(EditText)findViewById(R.id.et_search);
        mResultList=(RecyclerView)findViewById(R.id.result_list);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(this));

        bt_search=(Button)findViewById(R.id.bt_search);
        bt_token=(Button)findViewById(R.id.bt_token);


        dbWord = FirebaseDatabase.getInstance().getReference("word");
        dbReference=FirebaseDatabase.getInstance().getReference("word");
        mAuth=FirebaseAuth.getInstance();

        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(AddWord.this,MainActivity.class));

                }

            }
        };

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            mAuth.signOut();
            }
        });

        for (UserInfo profile : user.getProviderData()) {
            // Name, email address, and profile photo Url
            name = profile.getDisplayName();
            // String email = profile.getEmail();
            photoUrl = profile.getPhotoUrl();

        }

        tv_name.setText("Welcome "+name.toString());
        img_user.setImageURI(photoUrl);

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MacWord word=new MacWord();
                word.setUserName(name);
                word.setWord(et_word.getText().toString());
                word.setDescription(et_description.getText().toString());

                dbWord.push().setValue(word);


            }
        });

        bt_token.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String token= FirebaseInstanceId.getInstance().getToken();
                Log.d(TAG,"Token: "+ token);
                Toast.makeText(AddWord.this,token,Toast.LENGTH_LONG).show();
            }
        });

/*
        list=(ListView) findViewById(R.id.list1);
        zborovi=new ArrayList<MacWord>();
        wordAdapter = new ArrayAdapter<MacWord>(this, android.R.layout.simple_list_item_1,zborovi);


        dbWord.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot child:children) {
                    MacWord macWord = child.getValue(MacWord.class);
                    zborovi.add(macWord);

                }
                list.setAdapter(wordAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        }); */

    bt_search.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String searchText =et_search.getText().toString();
            fireBaseWordsSearch(searchText);
        }
    });


    }

    private void fireBaseWordsSearch(String searchText) {
        Query FirebaseSearchQuery=dbReference.orderByChild("word").startAt(searchText).endAt(searchText + "\uf8ff");
        FirebaseRecyclerAdapter<MacWord,WordsViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<MacWord, WordsViewHolder>(
                MacWord.class,
                R.layout.list_layout,
                WordsViewHolder.class,
                dbReference

        ) {
            @Override
            protected void populateViewHolder(WordsViewHolder viewHolder, MacWord model, int position) {

                viewHolder.setDetails(model.getUserName(),model.getWord(),model.getDescription());

            }
        };
        mResultList.setAdapter(firebaseRecyclerAdapter);

    }


    public class WordsViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public WordsViewHolder(View itemView) {
            super(itemView);

        mView=itemView;
        }

        public void setDetails(String userName,String word,String description){
            TextView user_name=(TextView)mView.findViewById(R.id.name_text);
            TextView word_text=(TextView)mView.findViewById(R.id.word_text);
            TextView description_text=(TextView)mView.findViewById(R.id.description_text);

            user_name.setText(userName);
            word_text.setText(word);
            description_text.setText(description);

        }



    }
    }
