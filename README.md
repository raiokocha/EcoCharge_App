# EcoCharge Aplicativo

<p>O desenvolvimento do projeto tem como objetivo entregar ao usuário uma forma simples e precisa de controle do consumo energético dos aparelhos eletrônicos utilizados em seu cotidiano. Utilizando tecnologias capazes de monitorar todo consumo de um aparelho diretamente na tomada, e apresentando ao usuário informações atualizadas e reais sobre gastos, EcoCharge surge como uma solução para ajudá-lo a administrar, da melhor forma, como seu dinheiro é gasto com aparelhos elétricos. O aplicativo foi desenvolvido para ser utilizado em sistemas Android 8.0 (Nougat), com suporte para versões anteriores até Android 6.0 (Marshmallow). Através do dispositivo (EcoSense) desenvolvido, o aplicativo consegue receber informações importantes sobre alterações no estado do monitoramento do aparelho conectado ao sensor, ou picos de correntes que possam estar ocorrendo durante o uso do mesmo, suportando uma carga de corrente de até 30 Amperes. Uma interface amigável, gráficos comparativos, histórico de consumo e um conjunto de informações dinâmicas são ferramentas disponibilizadas pelo aplicativo para que o usuário esteja sempre no controle de seus gastos.</p>

# Telas

<p>O aplicativo desenvolvido em Java para dispositivos android, utilizando a API 27.A principal função do aplicativo e apresentar toda informação coletado e pelo arduino e tratada pelo webservice.</p>
  
![alt text](https://user-images.githubusercontent.com/30579803/62866513-502b6800-bce7-11e9-96c0-89bfcb0cd08a.png) ![alt text](https://user-images.githubusercontent.com/30579803/62866498-46a20000-bce7-11e9-8da1-62d72978ca01.png) ![alt text](https://user-images.githubusercontent.com/30579803/62866524-54578580-bce7-11e9-9a26-b6b68ceac2db.png)

<p>Iniciando o aplicativo a primeira tela a aparecer será o login que foi optado somente pela api do google, levando em conta que sendo para android, todos os usuários possuem uma conta pré cadastrada no aparelho, facilitando o login. Adiante temos um breve tutorial de quatro fases explicando o seguimento do aplicativo, após o tutorial ele te redireciona para o menu principal onde encontra as principais funções do sistema.</p>

![alt text](https://user-images.githubusercontent.com/30579803/62867657-32133700-bcea-11e9-99d2-de20c70ff862.png) ![alt text](https://user-images.githubusercontent.com/30579803/62867659-34759100-bcea-11e9-95c5-1b0664878253.png) ![alt text](https://user-images.githubusercontent.com/30579803/62867664-38091800-bcea-11e9-82fc-e32d035a3c6f.png)

<p>Para facilidade de movimentação no aplicativo 90% das telas são fragments, que torna a navegação do usuário muito mais fluida e rápida. Existem três menus para auxiliar a troca de telas, o menu lateral, da tela inicial e duas opções no actionbar com atalhos para a tarifa e sobre os desenvolvedores do aplicativo.
 A tela de tarifa contém o campo para que o usuário preencha o valor da tarifa cobrada pelo distribuidora de energia da sua região, sendo obrigatória para a realizacao dos calculos, junto com o campo um breve tutorial onde encontrar o valor dessa taxa. 
</p>

![alt text](https://user-images.githubusercontent.com/30579803/62867884-b6fe5080-bcea-11e9-94bf-5e78bd942db2.png) ![alt text](https://user-images.githubusercontent.com/30579803/62867885-b9f94100-bcea-11e9-9136-0c689cec74a0.png) ![alt text](https://user-images.githubusercontent.com/30579803/62867891-be255e80-bcea-11e9-979b-5a0e435ca211.png)

<p>Próximo passo é ativar o ecosense, na tela do ecosense cadastre seu código de serial, que o dispositivo será vinculado com o aplicativo. Agora já pode cadastrar todos seus cômodos.</p>

![alt text](https://github.com/raiokocha/EcoCharge_App/issues/4) ![alt text](https://user-images.githubusercontent.com/30579803/62867977-f3ca4780-bcea-11e9-9128-0b4c1a89f828.png) ![alt text](https://user-images.githubusercontent.com/30579803/62867979-f62ca180-bcea-11e9-9ca0-a58a6226c9cb.png)

<p>A Partir dos cômodos criados e serial ativado, o cadastro dos aparelhos se torna possível. A tela de cadastro dos aparelhos permite selecionar um nome para o aparelho o cômodo onde o aparelho se encontra e escolher o ecosense para medição do aparelho, além de tudo isso também é possível escolher uma cor para que se acaso tenha vários ecosense em sua casa consiga diferenciados.</p>

![alt text](https://user-images.githubusercontent.com/30579803/62868058-23794f80-bceb-11e9-9e02-1c35c19c3c5a.png) ![alt text](https://user-images.githubusercontent.com/30579803/62868067-25dba980-bceb-11e9-8edf-a7373cf1f195.png) ![alt text](https://user-images.githubusercontent.com/30579803/62868074-27a56d00-bceb-11e9-8707-9470a26ceaa6.png)

<p>Enfim o resultado final do consumo do aparelho, para acessar seus gastos basta clicar em cima do seu aparelho na lista de aparelhos onde abrirá essas telas, onde apresentará quando o aparelho foi ligado e qual o tempo de duração ficou consumindo energia.Os calculos sao feito em tempo real, sempre mostrando valores corretos tanto em watts quanto em real. Para entreter mais o usuário existem estáticas que preveem o gasto por hora, dia e mês que só aparecem disponíveis quando o aparelho está ligado. 
</p>

<p>E por último um gráfico que compara os consumos de aparelhos do mesmo cômodo.</p>

![alt text](https://user-images.githubusercontent.com/30579803/62869819-ea42de80-bcee-11e9-847e-f112dde16af6.png) ![alt text](https://user-images.githubusercontent.com/30579803/62869824-ed3dcf00-bcee-11e9-960e-b9dc75af0546.png) ![alt text](https://user-images.githubusercontent.com/30579803/62869843-f169ec80-bcee-11e9-917f-77abe96c424c.png)

<p>Esta tela pode ser acessada direto do menu inicial, onde apresenta estatísticas gerais do consumo calculado, todos os aparelhos cadastrados que geraram consumo são somados e apresentados como uma grande conta de luz.
	E por último a tela sobre o aplicativo que descreve os desenvolvedores e apoiadores do projeto. 
</p>
