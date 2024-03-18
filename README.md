# Desafio Itaú API saldo e transferencias 

## Arquitetura

![arquitetura_cloud.png](arquitetura%2Farquitetura_cloud.png)

Explicando as motivacoes das escolhas da arquitetura:
1. API Gateway para controle de fluxo, versionamento e controle de consumidores (levando em consideracao a exigencia de algum token, por exemplo do STS);
2. Load Balancer para distruibuir o trafego das requisicoes que serao muitas, exige uma arquitetura parruda;
3. Como ponto de partida, seria necessario considerar uma configuracao com multiplas instancias de um tipo de maquina que ofereça um equilíbrio entre CPU e memoria, como as instancias da familia C5 ou M5, alem da escalabilidade, tendo em vista a demanda do enunciado do desafio, o ideal seria um teste de carga para ser mais preciso;
4. Pelos dados serem bem estruturado optei por um banco de dados relacional, particularmente optei especificamente pelo MySQL utilizando Aurora por conta de FinOps pois o custo é muito mais baixo que o RDS MySQL, com o mesmo desempenho. Seria necessario criar indices para uma melhor performance;
5. Como opcional para ganhar tempo de resposta alem do codigo estar reativo, vale analisar um cache na consulta da API de Cadastro visto que podem a ver dados que nao mudam com frequencia;
6. Sugeri um lambda para rodar um update na base todos os dias para ajusta o limite diario de um dia para o outro, somente em casos necessarios;
7. Sugeri tambem um topico de consumer kafka para escutar as notificacoes do Bacen no recebimento de transferencias para contas do banco Itau.

Vale ressaltar que seja uma API critica e que nao pode ficar fora, vale a pena configurar a aplicacao para ser Multi Region.

## Backlog

1. Adicionar um endpoint para listar todas as transferencias de uma conta em um determinado periodo;
2. Dependendo das regras de negocio as tabelas poderiam ter um relacionamento;
3. Como sugestao abordada acima, implementar o consumo de mensagens via kafka para receber transferencias e adicionar ao saldo do recebedor.


## Rodar o projeto
Para subir o docker, rodar o seguinte comando na raíz do projeto:

docker-compose up

VM Options:
-Dspring.profiles.active=local

O projeto está rodando na versao 17 do Java.




