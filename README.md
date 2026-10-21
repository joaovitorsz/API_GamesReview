Parte 2 do GamesReview: integração com PostgreSQL

Substituída a lista em memória pelo armazenamento no PostgreSQL, usando Spring Data JPA. Foram adicionados o mapeamento das entidades, o repository, a geração automática dos IDs e a busca por ID.

As rotas anteriores e os campos dos modelos foram mantidos. O cadastro, a consulta, a atualização e a exclusão passam a usar o banco, incluindo as avaliações dos jogos. Também foram adicionados os filtros combináveis por nome, gênero e Metacritic.
