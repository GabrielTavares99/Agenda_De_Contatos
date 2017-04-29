package com.ueuo.gabrieltavares.agendadecontatos.database;

/**
 * Created by gabri on 17/04/2017.
 */

public class ScriptSql {

    public static String getCreateContato(){

        //Arruma a junçaõ das nossas string sql
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE IF NOT EXISTS tb_contato(\n" +
                "\t_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT\n" +
                "\t,nome VARCHAR(200)\n"+
                "\t,telefone VARCHAR(14)\n" +
                "\t,tipoTelefone VARCHAR(1)\n" +
                "\t,email VARCHAR(255)\n" +
                "\t,tipoEmail VARCHAR(1)\n" +
                "\t,endereco VARCHAR(255)\n" +
                "\t,tipoEndereco VARCHAR(1)\n" +
                "\t,dataEspecial DATE\n" +
                "\t,tipoDataEspecial VARCHAR(1)\n" +
                "\t,grupo VARCHAR(255)\n" +
                ");");

        return sqlBuilder.toString();

    }

}
