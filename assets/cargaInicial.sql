INSERT INTO [categoria](_id, descricao, pagamento_automatico) values("1", "Alimentacao", "N");
INSERT INTO [categoria](_id, descricao, pagamento_automatico) values("2", "Lazer", "N");
INSERT INTO [categoria](_id, descricao, pagamento_automatico) values("3", "Saude", "N");
INSERT INTO [categoria](_id, descricao, pagamento_automatico) values("4", "Compras", "N");
INSERT INTO [categoria](_id, descricao, pagamento_automatico) values("5", "Condominio", "N");
INSERT INTO [categoria](_id, descricao, pagamento_automatico) values("6", "Educacao", "N");
INSERT INTO [categoria](_id, descricao, pagamento_automatico) values("7", "Aluguel", "N");
INSERT INTO [categoria](_id, descricao, pagamento_automatico) values("8", "Salario", "N");
INSERT INTO [categoria](_id, descricao, pagamento_automatico) values("9", "Emprestimos", "N");
INSERT INTO [categoria](_id, descricao, pagamento_automatico) values("10", "Agua", "N");
INSERT INTO [categoria](_id, descricao, pagamento_automatico) values("11", "Telefone", "N");
INSERT INTO [categoria](_id, descricao, pagamento_automatico) values("12", "Outros", "N");

INSERT INTO [contato_tipo](_id, descricao) values("1", "Amigos");
INSERT INTO [contato_tipo](_id, descricao) values("2", "Clientes");
INSERT INTO [contato_tipo](_id, descricao) values("3", "Colegas de Trabalho");
INSERT INTO [contato_tipo](_id, descricao) values("4", "Familia");
INSERT INTO [contato_tipo](_id, descricao) values("5", "Outros");
INSERT INTO [contato_tipo](_id, descricao) values("6", "Conhecidos");
INSERT INTO [contato_tipo](_id, descricao) values("7", "Fornecedores");
INSERT INTO [contato_tipo](_id, descricao) values("8", "Comercio");
INSERT INTO [contato_tipo](_id, descricao) values("9", "Funcionarios");

INSERT INTO [alarme]([_id], [descricao], [hora_inicial], [min_inicial] ,[hora_final], [min_final] ,[intervalo] ,[ativo], [dias_antes_vencimento]) VALUES("1", "Alarme Padrao", "08", "00","17","00" , "120", "S", "3");

