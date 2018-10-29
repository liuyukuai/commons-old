package com.itxiaoer.commons.neo4j;

import com.itxiaoer.commons.neo4j.repository.Neo4jRelationRepository;
import org.neo4j.ogm.session.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.support.Neo4jRepositoryFactory;

/**
 * @author : liuyk
 */
@Configuration
@SuppressWarnings("ALL")
public class Neo4jConfiguration {

    @Bean
    public Neo4jRelationRepository neo4jRelationRepository(Session session) {
        return neo4jRepositoryFactory(session).getRepository(Neo4jRelationRepository.class);
    }

    @Bean
    public Neo4jRepositoryFactory neo4jRepositoryFactory(Session session) {
        return new Neo4jRepositoryFactory(session);
    }
}
