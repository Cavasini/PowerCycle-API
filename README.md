# PowerCycle: Monitoramento e Otimização Energética para Veículos Elétricos

**PowerCycle** é uma aplicação RESTful desenvolvida em Java que visa monitorar e otimizar o consumo e a recuperação de energia em veículos elétricos. O sistema oferece relatórios detalhados e insights valiosos para promover a sustentabilidade no setor de transporte.

## 🚀 Objetivos

- **Monitorar** o desempenho energético durante viagens.
- **Calcular** energia recuperada, autonomia extra gerada e economia em recargas.
- **Fornecer relatórios detalhados** e intuitivos para análise de dados.
- **Contribuir para a sustentabilidade**, reduzindo desperdícios e emissões de carbono.

## 🛠️ Tecnologias Utilizadas

- **Java**
- **Maven** para gerenciamento de dependências.
- **JAX-RS** com **Jersey** para construção de APIs RESTful.
- **Oracle Database** para armazenamento dos dados.
- **Tomcat 10** como servidor de aplicação.

## 🔢 Funcionalidades

- Cadastro de viagens com dados coletados automaticamente por dispositivos IoT instalados no veículo.
- Consulta a informações de viagens e relatórios detalhados no aplicativo.
- Uso de uma API externa baseada no site [EV Database](https://ev-database.org/) para obter informações sobre bateria, consumo e eficiência dos veículos.

## 🔧 Cálculos Realizados

- **Energia Recuperada**: Soma da energia gerada por sistemas como frenagem regenerativa.
- **Autonomia Extra**: Distância adicional baseada na energia recuperada e na eficiência do veículo.
- **Economia de Energia**: Redução percentual no consumo total.
- **Tempo Economizado na Recarga**: Impacto no tempo de carregamento do veículo.

## 📂 Estrutura do Projeto

### Principais Classes

- `Trip`: Representa uma viagem.
- `EnergyRecovery`: Representa os dados de recuperação de energia associados.
- `TripDAO`: Gerencia interações com o banco de dados relacionadas a viagens.
- `EnergyRecoveryDAO`: Gerencia informações de recuperação de energia.

### Principais APIs

- `POST /api/v1/trips`: Cadastra uma nova viagem e processa os dados.
- `GET /api/v1/trips/{tripId}`: Retorna os detalhes de uma viagem específica.
- `GET /api/v1/recovery/{tripId}`: Lista recuperações de energia associadas a uma viagem.

## 📊 Diagrama de Fluxo

```plaintext
POST /api/v1/trips
    |
    +--> Busca dados do veículo na API externa (EV Database)
    |
    +--> Realiza cálculos específicos (energia recuperada, economia, etc.)
    |
    +--> Armazena os resultados no banco de dados
```

## 📈 Exemplos de Relatórios

- **Autonomia Extra**: `Energia Recuperada (kWh) ÷ Consumo Médio (kWh/km)`
- **Tempo Economizado na Recarga**: `Energia Recuperada ÷ Potência de Carregamento (kW)`

## 📲 Coleta de Dados

Os dados são gerados automaticamente por dispositivos IoT instalados no veículo. Esses dispositivos enviam informações como distância percorrida, consumo de energia e recuperação diretamente para a aplicação. O usuário pode acessar o aplicativo para visualizar viagens realizadas e consultar a economia obtida.

## 🤝 Contribuição

Contribuições são bem-vindas! Sinta-se à vontade para abrir issues ou enviar pull requests para melhorias.

## 🧑‍💻 Requisitos

- **Java 11+**
- **Apache Maven**
- **Tomcat 10**
- **Oracle Database**

## 📜 Licença

Este projeto está licenciado sob a [MIT License](LICENSE).

---

Este projeto foi desenvolvido como projeto de conclusão de semestre da matéria **DOMAIN DRIVEN DESIGN – JAVA** da faculdade FIAP, no curso de Engenharia de Software.


