package com.cotemig.fluo.ui.activities

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cotemig.fluo.R
import com.cotemig.fluo.app.FluoApp
import com.cotemig.fluo.helper.ImageHelper
import com.cotemig.fluo.helper.SDCardHelper
import com.cotemig.fluo.ui.fragments.MyTaskFragment
import com.cotemig.fluo.ui.fragments.NoTaskFragment
import com.cotemig.fluo.ui.fragments.ProjectsFragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.DexterBuilder
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException


class MainActivity : AppCompatActivity() {

    val millisInFuture: Long = 60000

    private val GALLERY = 1
    private val CAMERA = 2
    private var type: Int = 0
    private var pictureFile: String = ""
    lateinit var imaveViewChangeReference: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userwelcome.text = getString(
            R.string.textWelcome,
            FluoApp.account!!.name
        ) //        userwelcome.text = String.format(resources.getString(R.string.textWelcome, FluoApp.account!!.name))
        howManyTask.setText(R.string.textNoTasks)


        var url = "https://zcoin.io/wp-content/uploads/2017/01/blank-avatar-300x300.png"

//        var url = FluoApp.URL_IMAGE.plus(FluoApp.account!!.picture)




        Glide
            .with(this)
            .load(url)
            .apply(RequestOptions.circleCropTransform())
            .into(avatar)

//        Glide.with(this).load(url).into(avatar)

        btntask.setOnClickListener {
            setFragmentAndAddToBackstack(ProjectsFragment())
            btnhome.setColorFilter(resources.getColor(R.color.textmenuinactive))
            btntask.setColorFilter(resources.getColor(R.color.textmenu))
            tasktext.setTextColor(resources.getColor(R.color.textmenu))
            hometext.setTextColor(resources.getColor(R.color.textmenuinactive))
        }

        btnhome.setOnClickListener {
            setFragment(MyTaskFragment())
            btntask.setColorFilter(resources.getColor(R.color.textmenuinactive))
            btnhome.setColorFilter(resources.getColor(R.color.textmenu))
            hometext.setTextColor(resources.getColor(R.color.textmenu))
            tasktext.setTextColor(resources.getColor(R.color.textmenuinactive))
        }

        btnhome.callOnClick()


        avatar.setOnClickListener{

            photoClick(avatar, 1)

        }

//        createTimer()

    }

    fun photoClick(image: ImageView, type: Int){
        this.type = type
        imaveViewChangeReference = image
        showMenuDialog()
    }

    fun showMenuDialog(){

        Dexter.withActivity(this).withPermissions(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(object : MultiplePermissionsListener{


            override fun onPermissionsChecked(report: MultiplePermissionsReport) {

                if(report.areAllPermissionsGranted()){
                    showMenu()
                } else {
//                    "Foda-se fui...."
                }

            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>,
                token: PermissionToken
            ) {

                token.continuePermissionRequest()

            }
        }).check()

    }

    fun showMenu(){

        val pictureDialog = android.support.v7.app.AlertDialog.Builder(this)

        pictureDialog.setTitle("Selecione")

        val pictureDialogsItems = arrayOf("Tirar uma foto","Galeria de Fotos", "Sair do Fluo")

        pictureDialog.setItems(pictureDialogsItems) { dialog, which ->

            when(which){
                0 -> takePhotoCamera()
                1 -> choosePhotoFromGalery()
                2 -> exitFluo()
            }

        }

        pictureDialog.show()

    }

    fun takePhotoCamera(){

        var image = "fluoImage"

        val file = SDCardHelper.getSdCardFile(this, FluoApp.Directories.IMAGES, image + FluoApp.TYPE_IMAGE)

        pictureFile = file.absolutePath

        val uri = FileProvider.getUriForFile(this, FluoApp.FileProvider.authority, file)

        val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        i.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(i, CAMERA)

    }

    fun choosePhotoFromGalery(){

        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        // Imagem selecionada a partir da galeria
        data?.let{

            if(requestCode == GALLERY){

                val contentURI = it.data

                try{

                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, contentURI)
                    saveBitmapFromGallery(bitmap)

                }catch (e: IOException){

                }

            }

        }

        if(data == null && requestCode == CAMERA){
            saveBitmapFromCamera()
        }

    }

    fun saveBitmapFromGallery(bitmap: Bitmap){
        var bm = ImageHelper.cropToSquare(bitmap)
        var fileName = ImageHelper.imagePath("fluoImage").absolutePath
        var file = ImageHelper.saveImage(this, bm, fileName)

        // Deletar arquivo antes
        val myFile = File(file)

        Glide.with(this).load(myFile).apply(RequestOptions.circleCropTransform()).into(avatar)

        // sendFile(myFi)
    }

    fun saveBitmapFromCamera(){
        var bitmap = BitmapFactory.decodeFile(pictureFile)
        bitmap = ImageHelper.cropToSquare(bitmap)

        ImageHelper.saveImage(this, bitmap, pictureFile)

        var file = File(pictureFile)

        Glide.with(this).load(file).apply(RequestOptions.circleCropTransform()).into(avatar)


    }

    fun exitFluo(){

    }

//    fun createTimer() {
//        val countDownInterval: Long = 1000
//        timer(millisInFuture, countDownInterval).start()
//    }
//
//
//    fun timer(millisInFuture: Long, countDownInterval: Long): CountDownTimer {
//
//        return object : CountDownTimer(millisInFuture, countDownInterval) {
//
//            override fun onFinish() {
//
//                // TODO: fazer oq quiser, mensagem q o tempo exprirou, etc
//
//            }
//
//            override fun onTick(millisUntilFinished: Long) {
//                var perc = millisInFuture - millisUntilFinished
//                var progress = ((perc * 100) / millisInFuture)
//
//                progressAvatar.progress = progress.toInt()
//            }
//        }
//
//    }

    fun setFragment(f: Fragment) {
        var ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.content, f)
        ft.commit()
    }

    fun setFragmentAndAddToBackstack(f: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.addToBackStack(null)
        ft.replace(R.id.content, f)
        ft.commit()
    }
}
