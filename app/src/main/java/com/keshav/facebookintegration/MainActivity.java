package com.keshav.facebookintegration;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.*;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();
    private InterstitialAd interstitialAd;
    private RewardedVideoAd rewardedVideoAd;
    private NativeAd nativeAd;
    private AdView bannerAdView;
    public String BANNER_PLACEMENT_ID = "549167759165615_549192715829786";
    public String INT_PLACEMENT_ID = "549167759165615_549190905829967";
    public String RV_PLACEMENT_ID = "549167759165615_549192132496511";
    public String NATIVE_PLACEMENT_ID = "549167759165615_551513495597708";
    private NativeAdLayout nativeAdLayout;
    Button reqIS, reqRV, showIS, showRV, showNative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdSettings.setDebugBuild(true);
        AudienceNetworkAds.initialize(this);

        appUI();
        bannerAdListeners();
        interstitialAdListeners();
        rewardedVideoListeners();
        loadNativeAd();

    }

    // Application UI and button implementation.
    private void appUI() {
        reqIS = findViewById(R.id.ReqISButton);
        reqRV = findViewById(R.id.ReqRVButton);
        showIS = findViewById(R.id.ShowISButton);
        showRV = findViewById(R.id.ShowRVButton);
        showNative = findViewById(R.id.ShowNativeButton);

        showIS.setEnabled(false);
        showRV.setEnabled(false);
        showNative.setEnabled(false);

        reqIS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interstitialAd.loadAd();
            }
        });

        showIS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIntAd();
            }
        });

        reqRV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rewardedVideoAd.loadAd();
            }
        });

        showRV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rewardedVideoAd.show();
            }
        });

        showNative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inflateAd(nativeAd);
            }
        });
    }

    //Banner ad implementation
    private void bannerAdListeners() {
        bannerAdView = new AdView(this, BANNER_PLACEMENT_ID, AdSize.BANNER_HEIGHT_50);
        // Setting listeners for the Banner Ad
        bannerAdView.setAdListener(new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Error: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Ad loaded callback
                Log.d(TAG, "Banner ad is loaded.");
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Banner ad is clicked.");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Banner ad is displayed.");
            }
        });

        LinearLayout adContainer = findViewById(R.id.banner_container);
        adContainer.addView(bannerAdView);
        bannerAdView.loadAd();
    }

    //Interstitial ad implementation
    private void interstitialAdListeners() {
        interstitialAd = new InterstitialAd(this, INT_PLACEMENT_ID);
        // Setting listeners for the Interstitial Ad
        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.d(TAG, "Interstitial ad displayed.");
                showIS.setEnabled(false);
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.d(TAG, "Interstitial ad dismissed.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                showIS.setEnabled(true);
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        });
    }

    //Rewarded video ad implementation
    private void rewardedVideoListeners() {
        rewardedVideoAd = new RewardedVideoAd(this, RV_PLACEMENT_ID);
        // Set listeners for the Rewarded Video Ads
        rewardedVideoAd.setAdListener(new RewardedVideoAdListener() {
            @Override
            public void onError(Ad ad, AdError error) {
                // Rewarded video ad failed to load
                Log.e(TAG, "Rewarded video ad failed to load: " + error.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Rewarded video ad is loaded and ready to be displayed
                Log.d(TAG, "Rewarded video ad is loaded and ready to be displayed!");
                showRV.setEnabled(true);
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Rewarded video ad clicked
                Log.d(TAG, "Rewarded video ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                Log.d(TAG, "Rewarded video ad impression logged!");
                showRV.setEnabled(false);
            }

            @Override
            public void onRewardedVideoCompleted() {
                Log.d(TAG, "Rewarded video completed!");

            }

            @Override
            public void onRewardedVideoClosed() {
                Log.d(TAG, "Rewarded video ad closed!");

            }
        });
    }

    private void loadNativeAd() {
        nativeAd = new NativeAd(this, NATIVE_PLACEMENT_ID);
        // Set Native Ad Listner.
        nativeAd.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                // Native ad finished downloading all assets
                Log.d(TAG, "Native ad finished downloading all assets.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Native ad failed to load
                Log.e(TAG, "Native ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Native ad is loaded and ready to be displayed
                Log.d(TAG, "Native ad is loaded and ready to be displayed!");
                if (nativeAd == null || nativeAd != ad) {
                    return;
                }
                // Inflate Native Ad into Container
                showNative.setEnabled(true);
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Native ad clicked
                Log.d(TAG, "Native ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Native ad impression
                Log.d(TAG, "Native ad impression logged!");
                showNative.setEnabled(false);

            }
        });

        // native ad request
        nativeAd.loadAd();
    }

    private void showIntAd() {
        if (interstitialAd == null || !interstitialAd.isAdLoaded()) {
            return;
        }
        // Check if ad is already expired or invalidated, and do not show ad if that is the case. You will not get paid to show an invalidated ad.
        if (interstitialAd.isAdInvalidated()) {
            return;
        }
        // Show the ad
        interstitialAd.show();
    }

    private void inflateAd(NativeAd nativeAd) {
        nativeAd.unregisterView();

        // Add the Ad view into the ad container.
        nativeAdLayout = findViewById(R.id.native_ad_container);
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);

        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        View adView = inflater.inflate(R.layout.native_ad_layout, nativeAdLayout, false);
        nativeAdLayout.addView(adView);

        // Add the AdOptionsView
        LinearLayout adChoicesContainer = findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(MainActivity.this, nativeAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        AdIconView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
                adView,
                nativeAdMedia,
                nativeAdIcon,
                clickableViews);
    }

    @Override
    protected void onDestroy() {
        if (interstitialAd != null) {
            interstitialAd.destroy();
        }
        if (rewardedVideoAd != null) {
            rewardedVideoAd.destroy();
            rewardedVideoAd = null;
        }
        if (bannerAdView != null) {
            bannerAdView.destroy();
        }
        super.onDestroy();
    }
}