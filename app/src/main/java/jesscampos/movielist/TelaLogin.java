package jesscampos.movielist;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TelaLogin extends AppCompatActivity {

    //variables
    EditText edEmail, edSenha;
    Button btnLogar;
    TextView tvCadastrar;

    String url = "";
    String parametros = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_login);

        //variables just here
        edEmail = (EditText)findViewById(R.id.edEmail);
        edSenha = (EditText)findViewById(R.id.edSenha);
        btnLogar = (Button) findViewById(R.id.btnLogar);
        tvCadastrar = (TextView) findViewById(R.id.tvCadastrar);

        //evento cadastrar
        tvCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //abrir tela Cadastro
                Intent abreCadastro = new Intent(TelaLogin.this, TelaCadastro.class);
                startActivity(abreCadastro);
            }
        });

        //evento logar
        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //pra ver o estado da rede -- executa em segundo plano
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    // fetch data

                    String email = edEmail.getText().toString();
                    String senha = edSenha.getText().toString();

                    //verifica os campos e realiza o login
                    if(email.isEmpty() || senha.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Nenhum campo pode estar vazio", Toast.LENGTH_LONG).show();
                    } else {
                        url = "http://192.168.1.104/login/logar.php";
                        parametros = "email=" + email + "&senha=" + senha;
                        new SolicitaDados().execute(url);
                    }

                } else {
                    // display error
                    Toast.makeText(getApplicationContext(),"Nenhuma conexão detectada",Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    //atividade assincrona de assegurar a conexao
    private class SolicitaDados extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return Conexao.postDados(urls[0],parametros);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String resulto) {
            if(resulto.contains("login_ok")){
                //abrir tela Main
                Intent abreInicio = new Intent(TelaLogin.this,MainActivity.class);
                startActivity(abreInicio);
            } else {
                Toast.makeText(getApplicationContext(),"Usuario/senha incorretos",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
}
