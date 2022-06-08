package com.example.appbar.email

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import java.lang.Exception
import java.util.*
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


class SendEmail(var contexto: Context, var email: String, var subject: String, var message: String) : AsyncTask<Void, Void, Void>() {

    lateinit var sesion: Session
    lateinit var dialogoProgreso: ProgressDialog
    lateinit var props: Properties
    lateinit var config: Config

//---------------------------------------------------------------------------------------------------
    override fun onPreExecute() {
        super.onPreExecute()
        dialogoProgreso = ProgressDialog.show(contexto, "Enviando mensaje", "Por favor espere...", false, false)
    }

//---------------------------------------------------------------------------------------------------

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        dialogoProgreso.dismiss()
        Toast.makeText(contexto, "Mensaje enviado", Toast.LENGTH_LONG).show()
    }

//---------------------------------------------------------------------------------------------------

    override fun doInBackground(vararg params: Void?): Void? {
        props = Properties()
        props.put("mail.smtp.host", "smtp.gmail.com")
        props.put("mail.smtp.socketFactory.port", "465")
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
        props.put("mail.smtp.auth", "true")
        props.put("mail.smtp.port", "465")
        sesion = Session.getDefaultInstance(props, object : javax.mail.Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(config.EMAIL, config.PASSWORD)
            }
        })


        try {
            val mimeMessage = MimeMessage(sesion)
            config = Config()
            mimeMessage.setFrom(InternetAddress(config.EMAIL))
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false))
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message);
            Transport.send(mimeMessage)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

//---------------------------------------------------------------------------------------------------

}