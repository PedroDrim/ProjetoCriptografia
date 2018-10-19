# ProjetoCriptografia
Projeto para a matéria de segurança para o curso de engenharia de software ufg 2018-02.

# Objetivos
O objetivo deste projeto é criar uma forma de criptografia 'segura' por e-mail garantindo as seguintes características:
  1. **Garantir que a mensagem não foi alterada**: para isso se é gerado um hash da mensagem original baseado no algorítmo MD5.
  2. **Garantir a privacidade da mensagem**: para isso a mensagem original e a extensão do arquivo são criptografadas utilizando uma chave simétrica baseado no algorítmo AES-128.
  3. **Garantir a legitimidade do remetente**: para isso a chave simétrica utilizada na privacidade da mensagem  e o hash do arquivo original são criptografados por meio da chave privada do remetente.
  4. **Garantir a legitimidade do destinatário**: para isso o conjunto das informações acima (chave simétrica, hash, extensão do arquivo e mensagem) são criptografadas utilizando a chave pública do destinatário.
  
# Estrutura
O código possui algumas classes essenciais:
  1. **Um gerenciador de chave assimétrica**
  2. **Um gerenciador de chave simétrica**
  3. **Um gerenciador de hashes**
  4. **Um gerenciador geral de criptografia e descriptografia dos arquivos**
  5. **Uma estrutura nomeada de 'KeyWallet' para armazenar a chave privada do remetente e a chave pública do destinatário.**
  
Além disso cada classe do código está documentado além de possuírem seus respectivos tratadores próprios de excessão.
