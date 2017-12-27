package com.example.narasimman.contactsusingcontentproviders;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/*
Here we are displaying all the contacts of our mobile in the listview.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CheckUserPermsions();

    }


//getting the contact access permission from the user
    void CheckUserPermsions(){
        if ( Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) !=
                    PackageManager.PERMISSION_GRANTED  ){
                requestPermissions(new String[]{
                                android.Manifest.permission.READ_CONTACTS},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return ;
            }
        }

    }
    //get acces to location permsion
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readContacts();
                } else {
                    // Permission Denied
                    Toast.makeText( this,"permission denied" , Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    /*
    important snipets
     */

    ArrayList<ContactItem> arrayList=new ArrayList<>();
    public void readContacts(){
        Cursor cursor=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        //cusor holds all the list of contact object
        while (cursor.moveToNext()){
            String name=cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));//getting name of the contact
            String number=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            //getting phone number of the contact
            arrayList.add(new ContactItem(name,number));
            /*
            adding name and number to the contactItem entity class and adding the entire object to the arraylist
             */
        }

        CustomAdapter customAdapter=new CustomAdapter(arrayList);
        ListView listView=findViewById(R.id.listview);
        listView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();

    }

//custom adapter class for listview
    class CustomAdapter extends BaseAdapter {
        public  ArrayList<ContactItem>  contactItems ;

        public CustomAdapter(ArrayList<ContactItem>  listnewsDataAdpater) {
            this.contactItems=listnewsDataAdpater;
        }


        @Override
        public int getCount() {
            return contactItems.size();
        }

        @Override
        public String getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater mInflater = getLayoutInflater();
            View myView = mInflater.inflate(R.layout.list_item, null);

            final   ContactItem item = contactItems.get(position);

            TextView name=( TextView)myView.findViewById(R.id.name);
            name.setText(item.name);

            TextView number=myView.findViewById(R.id.number);
            number.setText(item.phoneNumber);

            return myView;
        }

    }
}
