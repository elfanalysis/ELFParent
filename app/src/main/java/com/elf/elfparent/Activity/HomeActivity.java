package com.elf.elfparent.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.elf.elfparent.R;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements Drawer.OnDrawerItemClickListener {


    Drawer mDrawer;
   /* @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.frag_holder)
    FrameLayout fragHolder;
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        // nadhaa

//        setUpDrawer();
    }

    private void setUpDrawer() {
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
//                .withHeaderBackground(R.drawable.header_img)
                .addProfiles(
                        new ProfileDrawerItem().withName("Student Name").withEmail("Standard").withIcon(R.drawable.ic_account_circle_black_24dp)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(0).withName("Home").withIcon(R.drawable.ic_home_black_24px)
                .withIconTintingEnabled(true);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(1).withName("Reports").withIcon(R.drawable.ic_accessment_24).withIconTintingEnabled(true);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(2).withName("Tests").withIcon(R.drawable.ic_assignment_black_24dp).withIconTintingEnabled(true);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(3).withName("Notifications").withIcon(R.drawable.ic_chat_bubble_black_24dp).withIconTintingEnabled(true);

        mDrawer = new DrawerBuilder()
                .withActivity(this)
//                .withToolbar(toolbar)
                .addDrawerItems(
                        item1, new DividerDrawerItem(),
                        item2, new DividerDrawerItem(),
                        item3, new DividerDrawerItem(),
                        item4, new DividerDrawerItem()
                )
                .withHasStableIds(true)
//                .withDrawerLayout(R.layout.material_drawer)
                .withActionBarDrawerToggle(false)
                .withOnDrawerItemClickListener(this)
//                .withDrawerLayout(R.layout.material_drawer)
                .withAccountHeader(headerResult)
                .build();

    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        Intent i =  null;
        if (drawerItem != null) {
            if (drawerItem.getIdentifier() == 0) {
                i = new Intent(this,HomeActivity.class);

            }


            if (drawerItem.getIdentifier() == 1) {
                i = new Intent(this,ReportActivity.class);
            }
            if (drawerItem.getIdentifier() == 2) {
                i = new Intent(this,TestReportsActivity.class);
            }
            if (drawerItem.getIdentifier() == 3) {
               i = new Intent(this,NotificationActivity.class);
            }

            startActivity(i);

            mDrawer.closeDrawer();



        }
        return true;
    }
    }

