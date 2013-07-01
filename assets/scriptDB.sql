CREATE TABLE [usuario] (
  [_id] INTEGER NOT NULL PRIMARY KEY, 
  [login] CHAR(50) NOT NULL, 
  [senha] CHAR(20), 
  [nome_exibicao] CHAR(30), 
  [login_automatico] CHAR(2) DEFAULT ('N'), 
  [email] VARCHAR(50));

CREATE INDEX [usuar_login] ON [usuario] ([login]);


CREATE TABLE [alarme] (
  [_id] INTEGER NOT NULL PRIMARY KEY, 
  [descricao] CHAR(40) NOT NULL, 
  [hora_inicial] INTEGER, 
  [min_inicial] INTEGER, 
  [hora_final] INTEGER, 
  [min_final] INTEGER, 
  [intervalo] INTEGER, 
  [ativo] CHAR(2) NOT NULL DEFAULT ('S'), 
  [usuario_id] INTEGER CONSTRAINT [usuario_alarme] REFERENCES [usuario]([_id]), 
  [dias_antes_vencimento] INTEGER);

CREATE INDEX [alarm_descricao] ON [alarme] ([descricao] ASC);

CREATE INDEX [alarm_usuario] ON [alarme] ([usuario_id] ASC);


CREATE TABLE [categoria] (
  [_id] INTEGER NOT NULL PRIMARY KEY, 
  [usuario_id] INTEGER CONSTRAINT [usuario_categoria] REFERENCES [usuario]([_id]), 
  [descricao] CHAR(40) NOT NULL, 
  [pagamento_automatico] CHAR(2) DEFAULT ('N'));

CREATE INDEX [categ_descricao] ON [categoria] ([descricao] ASC);

CREATE INDEX [categ_usuario] ON [categoria] ([usuario_id] ASC);


CREATE TABLE [conta] (
  [_id] INTEGER NOT NULL PRIMARY KEY, 
  [descricao] CHAR(40) NOT NULL, 
  [usuario_id] INTEGER CONSTRAINT [usuario_conta] REFERENCES [usuario]([_id]));

CREATE INDEX [conta_usuario] ON [conta] ([usuario_id] ASC);


CREATE TABLE [contato_tipo] (
  [_id] INTEGER NOT NULL PRIMARY KEY, 
  [usuario_id] INTEGER CONSTRAINT [usuario_contato_tipo] REFERENCES [usuario]([_id]), 
  [descricao] CHAR NOT NULL);

CREATE INDEX [ctotp_descricao] ON [contato_tipo] ([descricao] ASC);

CREATE INDEX [ctotp_usuario] ON [contato_tipo] ([usuario_id] ASC);


CREATE TABLE [contato] (
  [_id] INTEGER NOT NULL PRIMARY KEY, 
  [nome] CHAR(40) NOT NULL, 
  [usuario_id] INTEGER CONSTRAINT [usuario_contato] REFERENCES [usuario]([_id]), 
  [contato_tipo_id] INTEGER NOT NULL CONSTRAINT [contato_tipo_contato] REFERENCES [contato_tipo]([_id]), 
  [_id_contato_fone] INTEGER, 
  [sobrenome] VARCHAR(40), 
  [telefone] VARCHAR(14), 
  [email] VARCHAR(60));

CREATE INDEX [ctato_contato_tipo] ON [contato] ([contato_tipo_id] ASC);

CREATE INDEX [ctato_nome] ON [contato] ([nome] ASC);

CREATE INDEX [ctato_usuario] ON [contato] ([usuario_id] ASC);


CREATE TABLE [previsto] (
  [_id] INTEGER NOT NULL PRIMARY KEY, 
  [dt_emissao] CHAR(12) NOT NULL, 
  [val_previsto] DECIMAL(15, 2) NOT NULL, 
  [tipo_movimento] CHAR(2) NOT NULL, 
  [dt_vencimento] CHAR(12) NOT NULL, 
  [dt_ult_pagamento] CHAR(12), 
  [descricao] CHAR(60), 
  [val_saldo] DECIMAL(15, 2), 
  [usuario_id] CHAR CONSTRAINT [usuario_previsto] REFERENCES [usuario]([_id]), 
  [contato_id] CHAR CONSTRAINT [contato_previsto] REFERENCES [contato]([_id]), 
  [conta_id] CHAR NOT NULL CONSTRAINT [conta_previsto] REFERENCES [conta]([_id]), 
  [categoria_id] CHAR CONSTRAINT [categoria_previsto] REFERENCES [categoria]([_id]), 
  [alarme_id] INTEGER CONSTRAINT [alarme_previso] REFERENCES [alarme]([_id]), 
  [pagamento_automatico] CHAR(2) NOT NULL);

CREATE INDEX [previ_alarme] ON [previsto] ([alarme_id] ASC);

CREATE INDEX [previ_categoria] ON [previsto] ([categoria_id] ASC);

CREATE INDEX [previ_conta] ON [previsto] ([conta_id] ASC);

CREATE INDEX [previ_contato] ON [previsto] ([contato_id] ASC);

CREATE INDEX [previ_emissao] ON [previsto] ([dt_emissao] ASC);

CREATE INDEX [previ_usuario] ON [previsto] ([usuario_id] ASC);

CREATE INDEX [previ_vencimento] ON [previsto] ([dt_vencimento] ASC);

CREATE INDEX [previ_tipo_movimento] ON [previsto] ([tipo_movimento] ASC);


CREATE TABLE [realizado] (
  [_id] INTEGER NOT NULL PRIMARY KEY, 
  [dt_movimento] CHAR(12) NOT NULL, 
  [val_movimento] DECIMAL(15, 2) NOT NULL, 
  [tipo_movimento] CHAR(2) NOT NULL, 
  [descricao] CHAR(60), 
  [usuario_id] INTEGER CONSTRAINT [usuario_realizado] REFERENCES [usuario]([_id]), 
  [contato_id] INTEGER CONSTRAINT [contato_realizado] REFERENCES [contato]([_id]), 
  [conta_id] INTEGER NOT NULL CONSTRAINT [conta_realizado] REFERENCES [conta]([_id]), 
  [categoria_id] INTEGER CONSTRAINT [categoria_realizado] REFERENCES [categoria]([_id]), 
  [previsto_id] INTEGER CONSTRAINT [previsto_realizado] REFERENCES [previsto]([_id]));

CREATE INDEX [reali_categoria] ON [realizado] ([categoria_id] ASC);

CREATE INDEX [reali_conta] ON [realizado] ([conta_id] ASC);

CREATE INDEX [reali_contato] ON [realizado] ([contato_id] ASC);

CREATE INDEX [reali_movimento] ON [realizado] ([dt_movimento] ASC);

CREATE INDEX [reali_previsto] ON [realizado] ([previsto_id] ASC);

CREATE INDEX [reali_usuario] ON [realizado] ([usuario_id] ASC);

CREATE INDEX [reali_tipo_movimento] ON [realizado] ([tipo_movimento] ASC);


