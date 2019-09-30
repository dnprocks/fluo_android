package com.cotemig.fluo.ui.activities

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.Theme
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cotemig.fluo.R
import com.cotemig.fluo.app.FluoApp
import com.cotemig.fluo.database.fw.DAOBase
import com.cotemig.fluo.database.model.TOTask
import com.cotemig.fluo.helper.DateTime
import com.cotemig.fluo.helper.ImageHelper
import com.cotemig.fluo.helper.SDCardHelper
import com.cotemig.fluo.helper.SharedPreferencesHelper
import com.cotemig.fluo.helper.db.DBHelper
import com.cotemig.fluo.models.Account
import com.cotemig.fluo.services.RetrofitInitializer
import com.cotemig.fluo.ui.fragments.MyTaskFragment
import com.cotemig.fluo.ui.fragments.ProjectsFragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
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


//        var url = "https://zcoin.io/wp-content/uploads/2017/01/blank-avatar-300x300.png"

        var url = FluoApp.URL_IMAGE.plus(FluoApp.account!!.picture)


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

        btnaddtask.setOnClickListener {
//            AddTaskDialog.getDialog().show(supportFragmentManager, "")

            var i = Intent(this, AddTaskActivity::class.java)
            startActivity(i)

        }


        avatar.setOnClickListener {

            photoClick(avatar, 1)

        }

//        createTimer()
        testeSqlite()

    }

    fun testeSqlite(){

        var listTasks = DAOBase.list(this,TOTask(), "name")
        Toast.makeText(this, ""+ listTasks.size, Toast.LENGTH_LONG).show()

        var task = TOTask()
        task.id = DateTime.now().toString("yyyyMMddHHmmss")
        task.name = "Teste de tarefa"
        task.description = "Tarefa de teste com a descricao"
        task.priority = 1f
        task.createdAt = DateTime.now().millis.toFloat()
        task.idAccountTo = DateTime.now().toString()
        task.idProject = DateTime.now().toString()

        DAOBase.insert(this, task)

    }

    fun photoClick(image: ImageView, type: Int) {
        this.type = type
        imaveViewChangeReference = image
        showMenuDialog()
    }

    fun showMenuDialog() {

        Dexter.withActivity(this).withPermissions(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(object : MultiplePermissionsListener {


            override fun onPermissionsChecked(report: MultiplePermissionsReport) {

                if (report.areAllPermissionsGranted()) {
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

    fun downloadDatabase(){

        var s = RetrofitInitializer().serviceDatabase()
        var call = s.getDataBase()

        call.enqueue(object : retrofit2.Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {

                response?.let {

                    DBHelper.salvarBanco(this@MainActivity, it.body().byteStream())

                }

            }

            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {



            }

        })

    }

    fun showMenu() {

        val pictureDialog = android.support.v7.app.AlertDialog.Builder(this)

        pictureDialog.setTitle(R.string.select_menu)

        val pictureDialogsItems = arrayOf(
            getString(R.string.select_camera),
            getString(R.string.select_gallery),
            getString(R.string.select_download_database),
            getString(R.string.select_exit)
        )

        pictureDialog.setItems(pictureDialogsItems) { dialog, which ->

            when (which) {
                0 -> takePhotoCamera()
                1 -> choosePhotoFromGalery()
                2 -> downloadDatabase()
                3 -> exitFluo()
            }

        }

        pictureDialog.show()

    }

    fun takePhotoCamera() {

        var image = DateTime.now().toString("yyyyMMddHHmmss")

        Toast.makeText(this, image, Toast.LENGTH_LONG).show()

        val file =
            SDCardHelper.getSdCardFile(this, FluoApp.Directories.IMAGES, image + FluoApp.TYPE_IMAGE)

        pictureFile = file.absolutePath

        val uri = FileProvider.getUriForFile(this, FluoApp.FileProvider.authority, file)

        val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        i.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(i, CAMERA)

    }

    fun choosePhotoFromGalery() {

        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        // Imagem selecionada a partir da galeria
        data?.let {

            if (requestCode == GALLERY) {

                val contentURI = it.data

                try {

                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, contentURI)
                    saveBitmapFromGallery(bitmap)

                } catch (e: IOException) {

                }

            }

        }

        if (data == null && requestCode == CAMERA) {
            saveBitmapFromCamera()
        }

    }

    fun saveBitmapFromGallery(bitmap: Bitmap) {
        var bm = ImageHelper.cropToSquare(bitmap)
        var fileName = ImageHelper.imagePath(DateTime.now().toString("yyyyMMddHHmmss")).absolutePath
        var file = ImageHelper.saveImage(this, bm, fileName)

        // Deletar arquivo antes
        val myFile = File(file)

        Glide.with(this).load(myFile).apply(RequestOptions.circleCropTransform()).into(avatar)

         sendFile(myFile)
    }

    fun saveBitmapFromCamera() {
        var bitmap = BitmapFactory.decodeFile(pictureFile)

        var rotate = bitmap.height < bitmap.width

        bitmap = ImageHelper.cropToSquare(bitmap)

        ImageHelper.saveImage(this, bitmap, pictureFile)

        var file = File(pictureFile)

        var uri = Uri.fromFile(file)

        ImageHelper.normalizeImageForUri(this, uri, rotate)

        Glide.with(this).load(file).apply(RequestOptions.circleCropTransform()).into(avatar)
        sendFile(file)

    }

    fun sendFile(file: File) {

        var s = RetrofitInitializer().serviceAccount()

        var call = s.sendPhoto(
            image = MultipartBody.Part.createFormData(
                "fileName", file.name,
                RequestBody.create(MediaType.parse("image/*"), file)
            )
        )

        call.enqueue(object : retrofit2.Callback<Account> {

            override fun onResponse(call: Call<Account>?, response: Response<Account>?) {

            }

            override fun onFailure(call: Call<Account>?, t: Throwable?) {

            }

        })

    }


    // Envio de multiplos arquivos
    fun sendFile(file: List<File>, position: Int) {

        if (position > file.size) {


            var s = RetrofitInitializer().serviceAccount()

            var call = s.sendPhoto(
                image = MultipartBody.Part.createFormData(
                    "fileName", file[position].name,
                    RequestBody.create(MediaType.parse("image/*"), file[position])
                )
            )

            call.enqueue(object : retrofit2.Callback<Account> {

                override fun onResponse(call: Call<Account>?, response: Response<Account>?) {
                    sendFile(file, position + 1)
                }

                override fun onFailure(call: Call<Account>?, t: Throwable?) {

                }

            })
        } else {
            // TODO: Sucesso
        }

    }

    fun exitFluo() {

        MaterialDialog.Builder(this)
            .theme(Theme.LIGHT)
            .title(R.string.confirm)
            .content(R.string.content_exit)
            .positiveText(R.string.confirm_yes)
            .onPositive { dialog, which ->
                SharedPreferencesHelper.delete(this, "account", "userData")

                var intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)

                finish()
            }.negativeText(R.string.confirm_no)
            .show()

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
        ft.replace(R.id.content, f)
        ft.addToBackStack(null)
        ft.commit()
    }
}
