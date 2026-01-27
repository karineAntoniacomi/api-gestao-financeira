package br.com.gestao.financeira.api.domain.transacao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    @Query("""
        select new br.com.gestao.financeira.api.domain.transacao.DadosListagemTransacao(
            t.id, t.tipoTransacao, t.valor, t.dataTransacao, t.usuarioId
        )
        from Transacao t
        where t.ativo = true
    """)
    Page<DadosListagemTransacao> findAllAtivas(Pageable pageable);

}