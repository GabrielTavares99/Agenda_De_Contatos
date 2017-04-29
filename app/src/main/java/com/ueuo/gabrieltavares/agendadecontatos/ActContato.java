package com.ueuo.gabrieltavares.agendadecontatos;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.database.sqlite.*;
import android.widget.ListView;

import com.ueuo.gabrieltavares.agendadecontatos.app.MessageBox;
import com.ueuo.gabrieltavares.agendadecontatos.database.DataBase;
import com.ueuo.gabrieltavares.agendadecontatos.dominio.RepositorioContato;
import com.ueuo.gabrieltavares.agendadecontatos.dominio.entidades.Contato;

public class ActContato extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    private ImageButton imgBtnCadastrar;

    private DataBase dataBase;
    private SQLiteDatabase conexao;

    private ListView listaContatos;

    private EditText txt_pesquisa;

    private ContatoArrayAdapter adpContatos;
    private RepositorioContato repositorioContato;

    private static final String parametro_Contato = "CONTATO";

    MessageBox alertUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_contato);
        imgBtnCadastrar = (ImageButton) findViewById(R.id.imgBtn_cadastrar);

        imgBtnCadastrar.setOnClickListener(this);

        alertUsuario = new MessageBox(this);

        txt_pesquisa = (EditText) findViewById(R.id.txt_pesquisa);

        //Instanciando minha list view
        listaContatos = (ListView) findViewById(R.id.list_contatos);

        try {
            listaContatos.setOnItemClickListener(this);
        }catch (Exception e){
            alertUsuario.showAlert("Erro!","Erro ao carregar lista de contato: "+e.getMessage());
          //  alertUsuario.show();
        }

        try {
            dataBase = new DataBase(this);
            conexao = dataBase.getWritableDatabase();

            //Mensagem se deu certo a conexão ao banco de dados
            //alertUsuario.showInfo("Sucesso!","Banco criado com sucesso");

            //Instancia do objeto repositorio que possui os metodos de manupulação do banco
            repositorioContato = new RepositorioContato(conexao);

            //Dentro do arrayAdapter da lista eu faço a busca na classe
            //que possui os métodos de banco de dados
            //e busco os contatos salvos
            adpContatos = repositorioContato.buscarContatos(this);

            //Setando o meu arrayAdapter na minha list view
            listaContatos.setAdapter(adpContatos);

            //CLASSE NESTE ARQUIVO, RESPONSÁVEL POR IMPLEMENTAR UM TEXTCHANGEDLISTENER
            FiltrarDados filtrarDados = new FiltrarDados(adpContatos);
            //ADICIONANDO O EVENTO À CAIXA DE TEXTO
            txt_pesquisa.addTextChangedListener(filtrarDados);

        } catch (Exception e){
            //Caso dê erro na excução da conexao ao banco
            alertUsuario.showAlert("Erro!","Erro ao criar banco!"+e.getMessage());
        }

    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.imgBtn_cadastrar:

                Intent intent = new Intent(this,act_cadastroContato.class);
                startActivityForResult(intent,0);
                //startActivity(intent);
                break;
            default:
                break;
        }

    }

    //Método que trata os retorno de outras activitys
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Dentro do arrayAdapter da lista eu faço a busca na classe
        //que possui os métodos de banco de dados
        //e busco os contatos salvos
        adpContatos = repositorioContato.buscarContatos(this);

        //Setando o meu arrayAdapter na minha list view
        listaContatos.setAdapter(adpContatos);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //RECUPERANDO MEU OBJETO CONTATO SELECIONADO
        Contato contato = adpContatos.getItem(position);

        //INSTANCIO MINHA INTENT
        Intent intent = new Intent(this,act_cadastroContato.class);

        //PASSAGEM DO OBJETO CONTATO ENTRE TELAS
        //É PRECISO IMPLEMENTAR SERIALIZABLE NA CLASSE DO OBJETO A INTERFACE PRA TRANSPOSIÇÃO DE OBJETOS
        intent.putExtra(parametro_Contato,contato);

        //ABRO A SEGUNDA ACTIVITY
        startActivityForResult(intent,0);
    }

    //Classe para implementação do listener de texto
    private class FiltrarDados implements TextWatcher{

        ContatoArrayAdapter arrayAdapterContato;

        private FiltrarDados(ContatoArrayAdapter arrayAdapterContato){
            this.arrayAdapterContato = arrayAdapterContato;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //O PRÓPRIO ARRAYADAPTER TEM UMA FUNÇÃO DE FILTRAGEM
            //O PARÁMETRO DE FILTO SÃO OS CAMPOS DO MÉTODO .TOSTRING()
            arrayAdapterContato.getFilter().filter(s);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}

