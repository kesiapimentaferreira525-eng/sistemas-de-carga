CREATE TABLE IF NOT EXISTS tb_fatura (
    id BIGINT NOT NULL AUTO_INCREMENT,
    numero_fatura VARCHAR(255) NOT NULL,
    cnpj_cliente VARCHAR(255) NOT NULL,
    nome_cliente VARCHAR(255) NOT NULL,
    endereco_cliente VARCHAR(255),
    email_cliente VARCHAR(255),
    data_fatura DATE NOT NULL,
    data_vencimento DATE NOT NULL,
    data_pagamento DATE,
    subtotal DECIMAL(12, 2) NOT NULL,
    desconto DECIMAL(12, 2),
    total_faturado DECIMAL(12, 2) NOT NULL,
    dados_bancarios TEXT,
    status_pagamento VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_tb_fatura_numero_fatura UNIQUE (numero_fatura)
);

CREATE TABLE IF NOT EXISTS tb_servico_carga (
    id BIGINT NOT NULL AUTO_INCREMENT,
    material VARCHAR(255) NOT NULL,
    placa VARCHAR(255),
    data_servico DATE NOT NULL,
    ticket VARCHAR(255),
    unidade VARCHAR(255) NOT NULL,
    quantidade DOUBLE NOT NULL,
    valor_unitario DECIMAL(10, 2) NOT NULL,
    valor_total DECIMAL(10, 2) NOT NULL,
    cnpj_cliente VARCHAR(255) NOT NULL,
    nome_cliente VARCHAR(255) NOT NULL,
    data_pagamento DATE,
    status VARCHAR(255) NOT NULL,
    fatura_id BIGINT,
    PRIMARY KEY (id),
    INDEX idx_tb_servico_carga_fatura_id (fatura_id),
    CONSTRAINT fk_tb_servico_carga_fatura
        FOREIGN KEY (fatura_id) REFERENCES tb_fatura (id)
);
