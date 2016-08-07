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
import android.widget.Toast;

public class TelaCadastro extends AppCompatActivity {

    //variables
    EditText edNome,edEmailCadastro, edSenhaCadastro;
    Button btnCancelar, btnRegistrar;

    String url = "";
    String parametros = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_cadastro);

        //variables just here
        edNome = (EditText)findViewById(R.id.edNome);
        edEmailCadastro = (EditText)findViewById(R.id.edEmailCadastro);
        edSenhaCadastro = (EditText)findViewById(R.id.edSenhaCadastro);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);

        //evento
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //fechar essa tela e voltar pra tela de login
                finish();

            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pra ver o estado da rede -- executa em segundo plano
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    // fetch data

                    String nome = edNome.getText().toString();
                    String email = edEmailCadastro.getText().toString();
                    String senha = edSenhaCadastro.getText().toString();

                    //verifica se os campos nao estao vazios e realiza o login
                    if(nome.isEmpty() || email.isEmpty() || senha.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Nenhum campo pode estar vazio", Toast.LENGTH_LONG).show();
                    } else {
                        url = "http://192.168.1.104/login/registrar.php";
                        parametros = "nome="+ nome + "&email=" + email + "&senha=" + senha;
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
            if(resulto.contains("email_erro")){
                Toast.makeText(getApplicationContext(),"Email ja cadastrado",Toast.LENGTH_LONG).show();
            }else if(resulto.contains("registro_ok")){
                Toast.makeText(getApplicationContext(),"Registrado",Toast.LENGTH_LONG).show();

                //abrir tela Main
                Intent abreInicio = new Intent(TelaCadastro.this,MainActivity.class);
                startActivity(abreInicio);
            } else {
                Toast.makeText(getApplicationContext(),"Registro incorreto",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
}
