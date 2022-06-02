package com.example.sportssdbis

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.example.sportssdbis.databinding.ActivitySelectGeolocationBinding
import com.example.sportssdbis.utils.LocationPermissionHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.maps.*
import com.mapbox.maps.R
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import java.lang.ref.WeakReference

/**
 * Tracks the user location on screen, simulates a navigation session.
 */
class SelectGeolocation : AppCompatActivity() {

    //progress dialog
    private lateinit var progressDialog: ProgressDialog

    //firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var mapView: MapView
    private lateinit var binding: ActivitySelectGeolocationBinding

    private var lastStyleUri = Style.DARK
    private lateinit var locationPermissionHelper: LocationPermissionHelper
    private val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
        binding.mapView.getMapboxMap().setCamera(CameraOptions.Builder().center(it).build())
        binding.mapView.gestures.focalPoint = binding.mapView.getMapboxMap().pixelForCoordinate(it)
    }

    private val onIndicatorBearingChangedListener = OnIndicatorBearingChangedListener {
        mapView.getMapboxMap().setCamera(CameraOptions.Builder().bearing(it).build())
    }

    private val onMoveListener = object : OnMoveListener {
        override fun onMoveBegin(detector: MoveGestureDetector) {
            onCameraTrackingDismissed()
        }

        override fun onMove(detector: MoveGestureDetector): Boolean {
            return false
        }

        override fun onMoveEnd(detector: MoveGestureDetector) {}
    }

    private fun addCategoryFirebase() {
        //show progress
        progressDialog.setMessage("Adding location...")
        progressDialog.show()

        //get timestamp
        val timestamp = System.currentTimeMillis()

        //setup info to add in firebase db
        val hashMap = java.util.HashMap<String, Any>()

        //add to firebase db
        val ref = FirebaseDatabase.getInstance().getReference("Locations")
        ref.child("" + timestamp)
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(
                    this@SelectGeolocation,
                    "Location added successfully!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this@SelectGeolocation, "" + e.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupGesturesListener() {
        mapView.gestures.addOnMoveListener(onMoveListener)
    }

    private fun initLocationComponent() {
        val locationComponentPlugin = mapView.location
        locationComponentPlugin.updateSettings {
            this.enabled = true
            LocationPuck2D(
                bearingImage = AppCompatResources.getDrawable(
                    this@SelectGeolocation,
                    R.drawable.mapbox_user_puck_icon,
                ),
                shadowImage = AppCompatResources.getDrawable(
                    this@SelectGeolocation,
                    R.drawable.mapbox_user_icon_shadow,
                ),
                scaleExpression = interpolate {
                    linear()
                    zoom()
                    stop {
                        literal(0.0)
                        literal(0.6)
                    }
                    stop {
                        literal(20.0)
                        literal(1.0)
                    }
                }.toJson()
            ).also { this.locationPuck = it }
        }
        locationComponentPlugin.addOnIndicatorPositionChangedListener(
            onIndicatorPositionChangedListener
        )
        locationComponentPlugin.addOnIndicatorBearingChangedListener(
            onIndicatorBearingChangedListener
        )
    }

    private fun onCameraTrackingDismissed() {
        Toast.makeText(this, "onCameraTrackingDismissed", Toast.LENGTH_SHORT).show()
        mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView.location
            .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView.gestures.removeOnMoveListener(onMoveListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.location
            .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView.gestures.removeOnMoveListener(onMoveListener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectGeolocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        locationPermissionHelper = LocationPermissionHelper(WeakReference(this))
        locationPermissionHelper.checkPermissions {
            binding.mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS) {
                binding.mapView.gestures.scrollEnabled = false
                binding.mapView.gestures.addOnMapClickListener { point ->
                    binding.mapView.location
                        .isLocatedAt(point) { isPuckLocatedAtPoint ->
                            if (isPuckLocatedAtPoint) {
                                Toast.makeText(this, "Clicked on location puck", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    true
                }
                binding.mapView.gestures.addOnMapLongClickListener { point ->
                    binding.mapView.location
                        .isLocatedAt(point) { isPuckLocatedAtPoint ->
                            if (isPuckLocatedAtPoint) {
                                Toast.makeText(
                                    this,
                                    "Long-clicked on location puck",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    true
                }
                binding.mapView.gestures.addOnMapLongClickListener { point ->
                    binding.mapView.location
                        .isLocatedAt(point) { isPuckLocatedAtPoint ->
                            if (isPuckLocatedAtPoint) {
                                Toast.makeText(
                                    this,
                                    "Long-clicked on location puck",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    true
                }
            }
        }
    }

    private fun toggleMapStyle() {
        val styleUrl = if (lastStyleUri == Style.DARK) Style.LIGHT else Style.DARK
        binding.mapView.getMapboxMap().loadStyleUri(styleUrl) {
            lastStyleUri = styleUrl
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.location
            .addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
    }

    private fun addAnnotationToMap() {
// Create an instance of the Annotation API and get the PointAnnotationManager.
        bitmapFromDrawableRes(
            this@SelectGeolocation,
            R.drawable.mapbox_rounded_corner
        )?.let {
            val annotationApi = mapView?.annotations
            val pointAnnotationManager = annotationApi?.createPointAnnotationManager(mapView!!)
// Set options for the resulting symbol layer.
            val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
// Define a geographic coordinate.
                .withPoint(Point.fromLngLat(18.06, 59.31))
// Specify the bitmap you assigned to the point annotation
// The bitmap will be added to map style automatically.
                .withIconImage(it)
// Add the resulting pointAnnotation to the map.
            pointAnnotationManager?.create(pointAnnotationOptions)
        }
    }

    private fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
        convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))

    private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
        if (sourceDrawable == null) {
            return null
        }
        return if (sourceDrawable is BitmapDrawable) {
            sourceDrawable.bitmap
        } else {
// copying drawable object to not manipulate on the same reference
            val constantState = sourceDrawable.constantState ?: return null
            val drawable = constantState.newDrawable().mutate()
            val bitmap: Bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }
}
