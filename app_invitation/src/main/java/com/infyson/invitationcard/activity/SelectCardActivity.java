package com.infyson.invitationcard.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdView;
import com.infyson.invitationcard.R;
import com.infyson.invitationcard.adapter.CardAdapter;
import com.infyson.invitationcard.classes.AdmobBabberClass;
import com.infyson.invitationcard.classes.CardModel;
import com.infyson.invitationcard.classes.Constants;
import com.infyson.invitationcard.classes.CustomTextView;
import com.infyson.invitationcard.classes.ItemClickSupport;

import java.util.ArrayList;
import java.util.Objects;

public class SelectCardActivity extends AppCompatActivity {

    RecyclerView rlCardList;
    ArrayList androidVersions;
    ArrayList android_version;
    private ArrayList<CardModel> android_versions;

    private final String android_image_urls[] = {
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F10_general_2%20(1).jpg?alt=media&token=40aceea6-262f-4110-b31e-f289beec4630",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F10_general_2%20(2).jpg?alt=media&token=79570b8b-e04a-466b-a29c-97e008ea9735",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F10_general_2%20(22).jpg?alt=media&token=e4fef6d1-b577-461d-a666-9e1ff9f3090d",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F10_general_2%20(3).jpg?alt=media&token=4cd24840-098d-41b5-a9b3-ae36f8e10a2f",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F10_general_2%20(15).jpg?alt=media&token=b9a2500c-0217-4751-bddf-b39a225033cd",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F10_general_2%20(4).jpg?alt=media&token=78e835e6-d6b1-4306-9f4f-543cb79f8648",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F10_general_2%20(5).jpg?alt=media&token=cc8ea2ba-ca55-4e63-a37e-a31368148106",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F10_general_2%20(6).jpg?alt=media&token=909216ad-447b-48ec-8e8c-c3c2c3221a4e",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F10_general_2%20(9).jpg?alt=media&token=119fbb49-f3b9-4274-b6ca-03351de44ff6",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F10_general_1%20(1).jpg?alt=media&token=e36ce708-80bf-424a-ae7f-6f4edf3179a9",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F10_general_1%20(2).jpg?alt=media&token=426f9ac4-fedc-46d7-b64a-c7a8b13fdcfc",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F10_general_1%20(4).jpg?alt=media&token=9e5b27f7-f3b5-437e-b962-c7fbb1aa075b",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F10_general_1%20(5).jpg?alt=media&token=9b370c50-4828-4f72-a9c3-3b1278bafa5a",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F10_general_1%20(6).jpg?alt=media&token=e1aed3a4-7e25-4c45-8943-5fb6496c618f",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F10_general_1%20(7).jpg?alt=media&token=062415aa-61f6-457f-98a5-44c515c4d6cf",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F10_general_1%20(8).jpg?alt=media&token=e84bd81c-dca4-4d31-99d6-583d5e20a670",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F10_general_1.jpg?alt=media&token=ed87bed5-f429-41f1-a55c-b5374d7e5c2b",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_1.jpg?alt=media&token=14de8f49-cc44-41f6-a610-dbd7e40f37b7",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_10.jpg?alt=media&token=8aca1f74-4de2-46ce-b3ad-6bd2cd063b54",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_11.jpg?alt=media&token=a897ae5c-8782-4319-9891-11b688f3aabb",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_12.jpg?alt=media&token=db46bdca-7e29-4be8-a25a-dada8f7a91f9",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_13.jpg?alt=media&token=f4588133-55eb-4c52-bfb3-9cc08aa14931",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_15.jpg?alt=media&token=4801c321-f83e-4512-89ae-82b5621cabed",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_16.jpg?alt=media&token=32897f30-ddc9-4f45-9a03-878d9cc22e3b",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_17.jpg?alt=media&token=32320902-9770-4525-9e02-123216e5f738",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_18.jpg?alt=media&token=2c1e5d85-f14e-427e-bdf9-e7a04c3abf57",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F20_birthday%20(1).jpg?alt=media&token=7b444a38-85d4-4dab-aac5-ff2955086e57",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F20_birthday%20(2).jpg?alt=media&token=8981f539-f914-45bb-84b3-c9a3b0275326",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F20_birthday%20(3).jpg?alt=media&token=2e414a3e-5c0c-4aae-8ee6-fc87a69eb5ff",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F20_birthday%20(4).jpg?alt=media&token=9d33e402-2815-428a-b471-30ac69f6a840",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F20_birthday%20(6).jpg?alt=media&token=8ab685c6-146c-4fc5-8828-ecd433a8b582",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_19.jpg?alt=media&token=540bc416-7b49-4886-a5bf-ef7d86dd5e7c",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_2.jpg?alt=media&token=fb0b8712-783f-48ad-af47-dad2eb8c13df",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_20.jpg?alt=media&token=21a687ea-7b06-46b2-bae7-93cb7bbbfcb6",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_21.jpg?alt=media&token=b8a9be90-f059-48ee-93c9-530f01b2b5e3",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_23.jpg?alt=media&token=0ef55742-8491-4c40-8873-5f47451700f2",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_25.jpg?alt=media&token=03454567-01c9-4cb0-9178-53a9b7d42b27",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_26.jpg?alt=media&token=0b981f5b-4507-4cf3-99b0-7d968a896016",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_27.png?alt=media&token=c2650b5e-8044-42f3-b787-c6a2021f2c04",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_28.jpg?alt=media&token=8d9cf422-af13-4969-b85b-122827cf3db3",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_29.jpg?alt=media&token=0281b497-1548-4aad-abbe-35a894de7a00",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_3.jpg?alt=media&token=01375ad6-d833-40d4-a18d-303cb0c0bcac",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_30.jpg?alt=media&token=4efae426-37b9-4625-a68f-ff845103f661",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_31.jpg?alt=media&token=adf2cd66-99bd-4da1-bf32-3986b576fa2a",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_32.png?alt=media&token=e295542c-9855-4621-b07e-738411d5e71d",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_33.jpg?alt=media&token=b6cf8d3b-b354-4e1b-9012-9c20890c3817",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_34.png?alt=media&token=890f2219-dc1c-4fff-80bb-509c00437450",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_35.png?alt=media&token=0fc7b258-c6db-4e1d-9135-c464a146205a",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_36.jpg?alt=media&token=ff84dd7c-cd58-4380-b67d-eded886f408e",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_38.jpg?alt=media&token=5660cc55-75cc-4f74-815a-40bb71cd28ee",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_37.jpg?alt=media&token=07aaef0e-26c3-458f-96f7-cd6ac0feea64",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_4.jpg?alt=media&token=766c280f-6a3d-4e33-b6b5-967c27719559",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_40.jpg?alt=media&token=dd09d653-634c-42f4-ae56-53a9dfce899f",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_42.jpg?alt=media&token=880cd23e-9481-42e3-a07b-75780b37a88e",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_41.jpg?alt=media&token=5c5735ec-23c5-42f3-8094-b334a8271918",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_9.png?alt=media&token=4d5130fe-63cc-4f14-9de1-61e030eed49d",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_9.jpg?alt=media&token=bb349d78-b54e-493f-ab79-1a0db98fb4d7",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_7.png?alt=media&token=6071970f-bf07-48fa-a96f-1ac3531043d0",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_6.png?alt=media&token=1daa0503-27c0-490b-a1af-7d32c4ba24cb",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_50.jpg?alt=media&token=16b9aee5-b082-49b8-addd-1852bed3320e",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_5.png?alt=media&token=3b26fbef-0788-487f-90b2-5e6e27692754",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_49.jpg?alt=media&token=276d9550-0334-4955-b158-d4650cad43a8",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_48.jpg?alt=media&token=9971500a-bd61-47ca-a344-b601fb18d32e",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_46.jpg?alt=media&token=6b65b9e8-94ed-4b8f-abc0-a9d4b41ef50d",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_47.jpg?alt=media&token=244c3f5a-d1a1-4259-9d4e-c9abed667062",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_45.jpg?alt=media&token=bb29e0fd-0fdd-4264-945c-c746dc1f7e26",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_44.jpg?alt=media&token=43ad8c17-3861-4121-9672-9d7e5c3aca46",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2Fcard_43.jpg?alt=media&token=51aa3689-e4d2-4f4f-b09e-540e88e41303",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F10_general.jpg?alt=media&token=bb48abcc-a242-473a-933d-8ec5c813d9e4",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F11_pool%20(1).jpg?alt=media&token=b63f814d-533a-41fb-8469-2921df9386cd",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F11_pool%20(10).jpg?alt=media&token=f7732f0f-7ad6-4e48-b148-491a91bce324",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F11_pool%20(11).jpg?alt=media&token=e3ca16cd-6630-4743-85e6-fe70fa894aaa",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F11_pool%20(13).jpg?alt=media&token=2c531265-6b4a-4470-bff4-cd02f28c9c0d",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F11_pool%20(14).jpg?alt=media&token=c5a4f2f2-d5c9-4a40-ae78-6716c7dab66d",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F11_pool%20(15).jpg?alt=media&token=d170bfa2-9a6a-4c18-93fd-971727f67693",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F11_pool%20(16).jpg?alt=media&token=4d78d8f8-463d-45c5-af3f-55b8b5c1f7b6",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F11_pool%20(2).jpg?alt=media&token=2f1e1e8c-6808-42ab-b2da-a9d36cdddbec",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F11_pool%20(21).jpg?alt=media&token=b2fcedec-9f79-4727-a64b-9ad8fbdcd4dd",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F11_pool%20(22).jpg?alt=media&token=9285b8bc-f92e-40a7-9344-84003f8855ac",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F11_pool%20(25).jpg?alt=media&token=91c887db-3b9c-4bf7-83df-20a708ec16b6",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F11_pool%20(26).jpg?alt=media&token=50a08b55-5e52-4d73-bc0b-1d33964385ac",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F11_pool%20(27).jpg?alt=media&token=d018ea4b-4726-40ed-98a0-99dd6772f171",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F11_pool%20(28).jpg?alt=media&token=31dab9ff-dcd0-422a-b1d5-87608d9fe96d",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F11_pool%20(3).jpg?alt=media&token=2dd4fbc9-d157-4c0a-a138-b8df9ec4b093",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F11_pool%20(30).jpg?alt=media&token=97995fea-57e9-442a-ac63-12e35e6d7a3f",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F11_pool%20(4).jpg?alt=media&token=d2e6a93d-08c4-4921-900a-28a78d98aae0",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F11_pool%20(6).jpg?alt=media&token=64f1b05f-9def-4eda-875f-1b256f7db4d5",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F11_pool%20(7).jpg?alt=media&token=74129c5c-1ee8-42ab-a5f5-8e96cd588b5a",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F11_pool%20(8).jpg?alt=media&token=5a902682-f47a-48c7-98aa-a9290bc18567",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F11_pool%20(9).jpg?alt=media&token=15876699-6cb8-4030-9925-3a708c346bb8",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F12_barbecue%20(1).jpg?alt=media&token=57f84d80-41ed-48d4-8810-ae42943fd90d",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F12_barbecue%20(10).jpg?alt=media&token=8c8b05dc-e667-4b6a-a383-89c3e4e4f78f",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F12_barbecue%20(3).jpg?alt=media&token=0068e781-b8ee-49f3-9f97-568772bc9e1e",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F12_barbecue%20(4).jpg?alt=media&token=a01ef7c6-4ee4-4a42-b2f1-c87a3f225594",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F12_barbecue%20(5).jpg?alt=media&token=17c3bd43-c534-4ee7-b405-35b9770e2a85",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F12_barbecue%20(8).jpg?alt=media&token=47a52b11-0d6b-4df0-b956-0d43016ce288",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F12_barbecue%20(9).jpg?alt=media&token=f42a1520-7f3f-4201-afbb-6469ee262ba7",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F13_tea%20(1).jpg?alt=media&token=4203c292-3206-4971-97ec-42a9899e723a",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F13_tea%20(10).jpg?alt=media&token=eb3f43e4-9363-4026-9d0b-d1986f45957e",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F13_tea%20(2).jpg?alt=media&token=d4b7da72-4f03-40e1-a554-2b2bd8d3c7a7",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F13_tea%20(3).jpg?alt=media&token=b38af299-084b-463c-a041-7e5fa7bc89cf",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F13_tea%20(4).jpg?alt=media&token=05a268a5-a197-45c2-85c6-ca3cc1a72f0e",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F13_tea%20(5).jpg?alt=media&token=ffa2393a-6453-4e75-81e5-cc78257ee719",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F13_tea%20(6).jpg?alt=media&token=7a52c3d8-000a-4a23-8cb4-073b9ba2bba8",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F13_tea%20(7).jpg?alt=media&token=29675fe5-5c47-42ea-b5d9-8c92a90b49b1",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F13_tea%20(8).jpg?alt=media&token=a57e784e-43ee-473e-a0d5-bcce6171d9bb",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F13_tea%20(9).jpg?alt=media&token=7d4c34b2-e78e-40ae-9f4c-1e24aff0731e",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F14_dinner%20(1).jpg?alt=media&token=7ba9a044-5d07-4320-a268-cdeee7037292",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F14_dinner%20(10).jpg?alt=media&token=8f411374-62cc-4486-9775-67ffcf741a56",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F14_dinner%20(2).jpg?alt=media&token=71c616c3-0f5b-4306-8347-ed4ad640c114",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F14_dinner%20(4).jpg?alt=media&token=7b8a45b1-281b-41a0-886f-90d316383c0f",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F14_dinner%20(6).jpg?alt=media&token=a7303b30-f38f-416a-8886-a58eeacb6ae7",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F14_dinner%20(7).jpg?alt=media&token=05d62a0e-1a63-459e-9632-224e5934bc8a",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F14_dinner%20(8).jpg?alt=media&token=fc0e05d2-d04f-474b-a23d-77230e2a9afd",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F14_dinner%20(9).jpg?alt=media&token=b106ecad-bd8e-4092-afa6-b9618e588173",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F15_garden%20(1).jpg?alt=media&token=4ed728b3-dbda-4f6a-950b-90de89defef6",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F15_garden%20(10).jpg?alt=media&token=28d035d8-202b-4b38-8668-c21aacf10000",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F15_garden%20(11).jpg?alt=media&token=7a905240-00d6-4853-bb67-4f3acefd9d28",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F15_garden%20(12).jpg?alt=media&token=c6b80e51-8fe4-47e8-885a-cf2a8269f2ea",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F15_garden%20(13).jpg?alt=media&token=bede03f4-da91-4e2b-a246-4d3544c7c37f",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F15_garden%20(14).jpg?alt=media&token=111b8c37-01f5-46ce-acad-e5e35da16367",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F15_garden%20(15).jpg?alt=media&token=11a135d8-071c-47d6-8798-2d3bccef07e7",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F15_garden%20(17).jpg?alt=media&token=11b6b5f2-a3f3-4edf-bbfb-73cd46e96ced",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F15_garden%20(18).jpg?alt=media&token=89ac73b3-4463-4dac-8afb-8ba9644f68a0",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F15_garden%20(19).jpg?alt=media&token=6a044dd8-582b-45c9-9737-76c8d3d2e6c3",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F15_garden%20(2).jpg?alt=media&token=c8feb612-492a-40eb-8952-f8ac849a8943",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F15_garden%20(21).jpg?alt=media&token=0ec0f458-eae3-4405-a9c6-aed8b46e1300",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F15_garden%20(22).jpg?alt=media&token=32ef7f49-81c2-4246-b640-2c593ae3f0f2",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F15_garden%20(3).jpg?alt=media&token=fdbf9d59-d3c3-4182-93a7-3ce863d5c5e0",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F15_garden%20(3).jpg?alt=media&token=fdbf9d59-d3c3-4182-93a7-3ce863d5c5e0",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F15_garden%20(7).jpg?alt=media&token=9497be19-0d38-45e9-b041-ba83be9b423b",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F17_sleepover%20(1).jpg?alt=media&token=edd1a332-929e-4e5b-8de8-39fa84f8d045",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F17_sleepover%20(2).jpg?alt=media&token=370e99be-1bb8-47b5-975d-260ea8cb38be",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F17_sleepover%20(3).jpg?alt=media&token=0fbe32db-43a8-4d42-8e14-b9cc68a22ac0",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F17_sleepover%20(4).jpg?alt=media&token=3b22a152-966b-49d1-a552-12d2080423f8",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F17_sleepover%20(6).jpg?alt=media&token=50699be0-c57b-47ac-898d-b1e5844ddc40",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F17_sleepover%20(5).jpg?alt=media&token=5956d7f7-a75e-4c88-ae0d-6312db051b1e",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F17_sleepover%20(7).jpg?alt=media&token=e79c8235-d236-4298-a8c5-f1736e3f0bb2",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F16_music%20(1).jpg?alt=media&token=f7bb85dc-f3b2-402d-bbcd-c6fbd8d21ede",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F16_music%20(12).jpg?alt=media&token=25371fde-211c-4e8a-95af-f449c5f36360",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F16_music%20(13).jpg?alt=media&token=60c9334c-e7d8-4f73-8a89-2edb90dcb7fe",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F16_music%20(14).jpg?alt=media&token=535522a9-cad4-4a44-a995-c0f69d08003d",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F16_music%20(15).jpg?alt=media&token=ae4e9753-221f-43db-9808-b0ea87dac466",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F16_music%20(16).jpg?alt=media&token=3946cd51-50a0-42ef-bdf5-d8b25b3b21ac",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F16_music%20(17).jpg?alt=media&token=936fb245-aec3-43e4-9b00-9df67a21aaa0",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F16_music%20(18).jpg?alt=media&token=1141b047-fd67-4198-b88d-bbcf4696f7b1",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F16_music%20(19).jpg?alt=media&token=3c7b45a2-25a0-4c4f-8d09-b91d6b080267",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F16_music%20(2).jpg?alt=media&token=e2272f08-8459-45be-bba8-3148fe41cfe9",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F16_music%20(20).jpg?alt=media&token=0788ad0f-391d-4e59-9e2e-d7bf51af9cbe",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F16_music%20(3).jpg?alt=media&token=3beb1526-320c-4a0f-9219-e97bc2271853",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F16_music%20(4).jpg?alt=media&token=c61ccd8d-935d-48f9-b387-a1c587391bde",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F16_music%20(5).jpg?alt=media&token=fa47c2db-c165-456c-90b6-83edf8b1c2a1",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F16_music%20(6).jpg?alt=media&token=13d7d0e0-d202-4c12-b6a0-c39e81328b1f",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F16_music%20(7).jpg?alt=media&token=0a3556be-6de9-4a39-afc0-4d37bef54c5e",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F16_music%20(8).jpg?alt=media&token=261a27ac-062a-4458-8026-d323f00c983b",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F16_music%20(9).jpg?alt=media&token=7055d7ed-54a7-4fcb-a0a5-a9f2c89fb963",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F18_movie%20(1).jpg?alt=media&token=8d256a22-32da-4a5f-89e8-15ea673346d3",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F18_movie%20(2).jpg?alt=media&token=d438d1c4-224b-4cf2-81b4-8ba96a2303ec",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F18_movie%20(3).jpg?alt=media&token=c5ec5d7a-7d78-4091-a029-01f08b7ecced",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F18_movie%20(4).jpg?alt=media&token=98396fd5-e994-42ed-b86d-8200d9e49868",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F18_movie%20(5).jpg?alt=media&token=cc14c091-00c3-4bf7-a9a4-7575eeafaaeb",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F18_movie%20(6).jpg?alt=media&token=61c86951-371c-456d-b6b7-b7615f9acea4",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F18_movie%20(7).jpg?alt=media&token=6423e51d-e1e7-463d-ad49-29f1d530656a",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F18_movie%20(8).jpg?alt=media&token=de286ce3-dada-44ee-922d-60e40425e1e3",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F18_movie%20(9).jpg?alt=media&token=076727d4-944b-458e-8a05-91821e673eee",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F18_movie.jpg?alt=media&token=d371fc4e-03d7-4c1f-8e27-3fccdb67748a",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F19_cocktail%20(1).jpg?alt=media&token=316c6452-3e74-4db3-88b4-be5522c3b0f6",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F19_cocktail%20(10).jpg?alt=media&token=010418df-02ec-4831-baff-ac8f0a0a86c2",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F19_cocktail%20(11).jpg?alt=media&token=930e4319-6c87-45a6-9247-5244b23d10f2",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F19_cocktail%20(12).jpg?alt=media&token=82c81028-97d1-4a83-82ff-33389b166a2d",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F19_cocktail%20(13).jpg?alt=media&token=a861ffe3-d10e-49eb-a9e6-e9cfb815e2fd",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F19_cocktail%20(14).jpg?alt=media&token=b9fab48a-4c92-4aa2-840b-0643c40fde49",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F19_cocktail%20(15).jpg?alt=media&token=be3cbadf-9fc4-4459-8128-1ae15e1a5de4",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F19_cocktail%20(16).jpg?alt=media&token=ba8f9fcf-d9ee-4e5a-b854-3029d4437820",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F19_cocktail%20(17).jpg?alt=media&token=2be840ad-ab91-4ff2-8c62-5824d20eaaf9",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F19_cocktail%20(18).jpg?alt=media&token=141f8620-89ce-498e-a7f2-24e9cf761ccf",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F19_cocktail%20(19).jpg?alt=media&token=a162eda0-491f-40f3-b704-f26956386b71",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F19_cocktail%20(2).jpg?alt=media&token=f8a4534d-b891-4ca1-9efe-8d429c4e3fb0",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F19_cocktail%20(3).jpg?alt=media&token=54e8bbc8-5d8f-487c-b851-09c0b595decb",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F19_cocktail%20(4).jpg?alt=media&token=83df4857-45ad-48ea-a8b9-c09f57e83ec2",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F19_cocktail%20(5).jpg?alt=media&token=f07bb597-7a82-4567-896f-d38e4aa9f0a8",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F19_cocktail%20(6).jpg?alt=media&token=4f8cf9e9-1d5d-4d66-a50a-d980166cd4b3",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F19_cocktail%20(7).jpg?alt=media&token=751299e2-56e7-4f8b-a2ba-cef9f45e0a50",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F19_cocktail%20(8).jpg?alt=media&token=0445cd4a-c47e-4d06-9110-1562eb8660ed",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F22_graduation%20(1).jpg?alt=media&token=29a6ce15-4c42-4a3c-aaeb-f1529bbe6330",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F22_graduation%20(2).jpg?alt=media&token=6b504e13-8c1d-4868-b745-404f9d2c4107",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F22_graduation%20(3).jpg?alt=media&token=f98ecaea-47df-47e9-8e88-f97d46eb72d2",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F22_graduation%20(4).jpg?alt=media&token=3375fa40-f48a-452e-b0db-93c74367c162",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F22_graduation%20(5).jpg?alt=media&token=001b5b2b-baf5-44e7-83ae-4aa3decf7a41",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F22_graduation%20(6).jpg?alt=media&token=29c7f90a-d6e8-4245-b6c3-7edc2bba0be2",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F22_graduation%20(7).jpg?alt=media&token=86f0673d-7175-45cb-bcf5-0743856e7729",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F10_general_2%20(17).jpg?alt=media&token=48edf9b9-cebf-4ddb-a060-830b8dc67de9",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F10_general_2%20(18).jpg?alt=media&token=65301f26-773d-4eb8-86cc-8503ec33efe4",
            "https://firebasestorage.googleapis.com/v0/b/invitationcard-6c694.appspot.com/o/newcard%2F10_general_2%20(19).jpg?alt=media&token=7a37830b-41ce-4c39-ac08-7384c610aae7",


    };

    AdView mAdView;
    RelativeLayout bannerAdContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_card);
        actionBar();
        init();
        AdmobBabberClass.BannerAdShow(getApplicationContext(),bannerAdContainer);

        if (Build.VERSION.SDK_INT >= 23) {
            if (!checkPermissionForStroage()) {
                requestPermissionForStroage();
            }
        }
        

    }


    private void actionBar() {

        try{


            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.title_preview, null);
            ((CustomTextView)v.findViewById(R.id.tv_actionbar_title)).setCustomText(R.string.activity_select_card, Constants.FOURTH_FONT);
            this.getSupportActionBar().setCustomView(v);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            this.getSupportActionBar().setDisplayShowCustomEnabled(true);
            this.getSupportActionBar().setDisplayShowTitleEnabled(false);

        }catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private void init() {
        android_version = new ArrayList<>();
        android_versions = new ArrayList<>();

        bannerAdContainer= findViewById(R.id.bannerAdContainer);
        rlCardList = (RecyclerView) findViewById(R.id.rl_card_list);
        rlCardList.setHasFixedSize(true);
        rlCardList.setLayoutManager(new GridLayoutManager(this, 2));
        androidVersions = prepareData();
        CardAdapter adapter = new CardAdapter(SelectCardActivity.this, androidVersions);
        rlCardList.setAdapter(adapter);

        ItemClickSupport.addTo(rlCardList).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                startActivity(new Intent(SelectCardActivity.this, ParthThemeActivity.class).putExtra("CardUrl",android_versions.get(position).getAndroid_image_url()));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
    }

    private ArrayList prepareData() {
        for (int i = 0; i < android_image_urls.length; i++) {
            CardModel androidVersion = new CardModel();
            androidVersion.setAndroid_image_url(android_image_urls[i]);
            android_version.add(androidVersion);
            android_versions.add(androidVersion);
        }
        return android_version;
    }

    public boolean checkPermissionForStroage() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public void requestPermissionForStroage() {
        ActivityCompat.requestPermissions(SelectCardActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                Constants.STORAGE_PERMISSION_CODE);
    }

    private boolean checkPermissionForReadPhone() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermissionForReadPhone() {
        ActivityCompat.requestPermissions(SelectCardActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE},
                Constants.READ_PHONE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.STORAGE_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   // callSyncProcess();
                } else {
                    requestPermissionForStroage();
                }
                break;
            case Constants.READ_PHONE_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   // doDeviceInfoApiCall();
                } else {
                    requestPermissionForReadPhone();
                }
                break;
        }
    }

}
