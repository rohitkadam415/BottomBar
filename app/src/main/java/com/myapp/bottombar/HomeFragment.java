package com.myapp.bottombar;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";
  private static final String IMAGE_FOLDER = "/Profile/";
  private static final int REQUEST_CAMERA = 1234;
  private static final int REQUEST_GALLERY = 9162;
  private static final int PERMISSIONS_REQUEST_WRITE_STORAGE = 778;
  private static final String IMAGE_TYPE = "image/*";

  static String JPG_EXTENSION = ".jpg";
  static String PROVIDER = ".provider";

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;
  private Button cameraButton;

  private OnFragmentInteractionListener mListener;
  private Uri profileImageUri;
  private String TAG = HomeFragment.this.getClass().getSimpleName();
  private Uri mImageURI;
  private List<Integer> photosUrl=new ArrayList<>();

  public HomeFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment HomeFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static HomeFragment newInstance(String param1, String param2) {
    HomeFragment fragment = new HomeFragment();
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
    View view = inflater.inflate(R.layout.fragment_home, container, false);
    cameraButton = view.findViewById(R.id.camera);
    cameraButton.setOnClickListener(this);

    // ....Viewpager for sliding images
    photosUrl.add(R.mipmap.ic_launcher);
    photosUrl.add(R.mipmap.ic_launcher);
    photosUrl.add(R.mipmap.ic_launcher);

    ViewPager pager = view.findViewById(R.id.photos_viewpager);
    CustomPagerAdapter adapter = new CustomPagerAdapter(getContext(), photosUrl);
    pager.setAdapter(adapter);

    TabLayout tabLayout = view.findViewById(R.id.tab_layout);
    tabLayout.setupWithViewPager(pager, true);

    return view;
  }

  // TODO: Rename method, update argument and hook method into UI event
  public void onButtonPressed(Uri uri) {
    if (mListener != null) {
      mListener.onFragmentInteraction(uri);
    }
  }

  @Override
  public void onClick(View v) {
    if (ContextCompat.checkSelfPermission(getActivity(),
        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
      requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
          PERMISSIONS_REQUEST_WRITE_STORAGE);
    } else {
      selectImageDialog();
    }
  }

  private void selectImageDialog() {
    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
    View.OnClickListener listener = new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        switch (v.getId()) {
          case R.id.tv_camera:
            bottomSheetDialog.dismiss();
            openCamera();
            break;
          case R.id.tv_gallery:
            bottomSheetDialog.dismiss();
            opengalleryIntent();
            break;
          case R.id.closeBtn:
            bottomSheetDialog.dismiss();
            break;
        }
      }
    };

    bottomSheetDialog.setContentView(R.layout.bottom_dialog_view);
    bottomSheetDialog.show();
    bottomSheetDialog.findViewById(R.id.tv_camera).setOnClickListener(listener);
    bottomSheetDialog.findViewById(R.id.tv_gallery).setOnClickListener(listener);
    bottomSheetDialog.findViewById(R.id.tv_remove).setOnClickListener(listener);
    bottomSheetDialog.findViewById(R.id.closeBtn).setOnClickListener(listener);
    bottomSheetDialog.findViewById(R.id.tv_remove).setVisibility(View.GONE);
  }


  public interface OnFragmentInteractionListener {
    // TODO: Update argument type and name
    void onFragmentInteraction(Uri uri);
  }

  private void openCamera() {
    profileImageUri = generateTimeStampPhotoFileUri(getContext());
    openCameraIntent(profileImageUri);
  }

  private void openCameraIntent(Uri profileImageUri) {
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    intent.putExtra(MediaStore.EXTRA_OUTPUT, profileImageUri);
    getActivity().startActivityForResult(intent, REQUEST_CAMERA);

  }

  private void opengalleryIntent() {
    Intent intent = new Intent(Intent.ACTION_PICK);
    intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        IMAGE_TYPE);
    if (null != intent.resolveActivity(getContext().getPackageManager())) {
      startActivityForResult(intent, REQUEST_GALLERY);
    } else {
    }
  }

  /*   * its start to Crop image
   */
  private void beginCrop(Uri source) {
    long milli = System.currentTimeMillis();
    File file = new File(getActivity().getCacheDir(), getString(R.string.child_crop) + milli);
    Crop.of(source, Uri.fromFile(file)).asSquare().start(getActivity());
  }

  /*
   * Handle Crop after crop image
   */
  private void handleCrop(int resultCode, Intent result) {
    if (resultCode == Activity.RESULT_OK) {
      mImageURI = Crop.getOutput(result);
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch (requestCode) {
      case PERMISSIONS_REQUEST_WRITE_STORAGE:
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          // permission was granted, yay! Do the
          selectImageDialog();
        } else {
          // permission denied, boo! Disable the
          Log.i(TAG, "onRequestPermissionsResult: ");
        }
        break;
    }
  }

  public static Uri generateTimeStampPhotoFileUri(Context context) {

    File direct = getPhotoDirectory();
    Uri photoFileUri = null;
    if (direct != null) {
      Time t = new Time();
      t.setToNow();
      File photoFile = new File(direct, System.currentTimeMillis() + JPG_EXTENSION);
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        photoFileUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID +
            PROVIDER, photoFile);
      } else {
        photoFileUri = Uri.fromFile(photoFile);
      }
    }
    return photoFileUri;
  }

  private static File getPhotoDirectory() {
    File outputDir = null;
    String externalStorageStagte = Environment.getExternalStorageState();
    if (externalStorageStagte.equals(Environment.MEDIA_MOUNTED)) {
      outputDir =
          new File(Environment.getExternalStorageDirectory() + IMAGE_FOLDER);
      if (!outputDir.exists()) {
        outputDir =
            new File(
                Environment.getExternalStorageDirectory().getPath() + IMAGE_FOLDER);
        outputDir.mkdirs();
      }
    }
    return outputDir;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (Activity.RESULT_OK == resultCode) {
      switch (requestCode) {
        case REQUEST_GALLERY:
          beginCrop(data.getData());
          break;
        case REQUEST_CAMERA:
          beginCrop(profileImageUri);
          break;
        case Crop.REQUEST_CROP:
//          EchoMeImageUtils.setProfileImage(mIVProfilePic, String.valueOf(Crop.getOutput(data)));
//          Glide.with(getContext()).
//              load(String.valueOf(Crop.getOutput(data))).apply(new RequestOptions()
//              .transform((new BlurTransformation(EchoMeApplication.getEchoMeContext())))).into
//              (mIVCoverPic);
//
          handleCrop(resultCode, data);
          break;
      }
    }
  }

}
