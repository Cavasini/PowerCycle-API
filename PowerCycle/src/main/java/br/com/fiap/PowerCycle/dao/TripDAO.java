package br.com.fiap.PowerCycle.dao;

import br.com.fiap.PowerCycle.exception.TripException;
import br.com.fiap.PowerCycle.model.Trip;

import java.sql.SQLException;
import java.util.List;

public interface TripDAO {

    /**
     * Busca todas as viagens com suas respectivas recuperações de energia.
     * 
     * @return Lista de viagens com as recuperações associadas.
     * @throws SQLException em caso de falha na consulta ao banco de dados.
     */
    List<Trip> fetchTripsWithRecoveries() throws SQLException;

    /**
     * Busca uma viagem específica pelo ID, incluindo as recuperações de energia associadas.
     * 
     * @param tripId ID da viagem a ser buscada.
     * @return Objeto Trip com as informações da viagem e suas recuperações.
     * @throws SQLException em caso de falha na consulta ao banco de dados.
     */
    Trip fetchTripById(String tripId) throws SQLException;

    /**
     * Registra uma nova viagem e suas recuperações associadas no banco de dados.
     * 
     * @param trip Objeto Trip a ser registrado.
     * @return O ID gerado para a nova viagem.
     * @throws TripException em caso de falha na validação ou no registro.
     */
    String registerTrip(Trip trip) throws TripException;

    /**
     * Atualiza as informações de uma viagem no banco de dados.
     * 
     * @param trip Objeto Trip com os novos dados a serem atualizados.
     * @throws SQLException em caso de falha na atualização.
     */
    void updateTrip(Trip trip) throws SQLException;

    /**
     * Exclui uma viagem e suas recuperações associadas do banco de dados.
     * 
     * @param tripId ID da viagem a ser excluída.
     */
    void deleteTripById(String tripId) throws SQLException ;
}
