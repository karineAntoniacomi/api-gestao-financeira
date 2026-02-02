package br.com.gestao.financeira.service.transacao.repository;


import br.com.gestao.financeira.service.transacao.domain.TipoTransacao;
import br.com.gestao.financeira.service.transacao.domain.Transacao;
import br.com.gestao.financeira.service.transacao.dto.DadosListagemTransacao;
import br.com.gestao.financeira.service.transacao.dto.DadosResumoCategoria;
import br.com.gestao.financeira.service.transacao.dto.DadosResumoMensal;
import br.com.gestao.financeira.service.transacao.dto.ResumoDiarioProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    @Query("""
                select new br.com.gestao.financeira.service.transacao.dto.DadosListagemTransacao(
                    t.id, t.tipoTransacao, t.valor, t.dataTransacao, t.usuarioId, t.status
                )
                from Transacao t
                where t.ativo = true
            """)
    Page<DadosListagemTransacao> findAllAtivas(Pageable pageable);

    @Query("""
                select new br.com.gestao.financeira.service.transacao.dto.DadosResumoCategoria(
                    t.tipoTransacao,
                    sum(t.valor)
                )
                from Transacao t
                where t.ativo = true
                  and t.usuarioId = :usuarioId
                  and t.tipoTransacao in :tipos
                group by t.tipoTransacao
            """)
    List<DadosResumoCategoria> totalPorCategoria(
            @Param("usuarioId") Long usuarioId,
            @Param("tipos") List<TipoTransacao> tipos
    );

    @Query("""
                select
                    function('date', t.dataTransacao) as data,
                    sum(t.valor) as total
                from Transacao t
                where t.ativo = true
                  and t.usuarioId = :usuarioId
                  and t.tipoTransacao in :tipos
                group by function('date', t.dataTransacao)
                order by function('date', t.dataTransacao)
            """)
    List<ResumoDiarioProjection> totalPorDia(
            @Param("usuarioId") Long usuarioId,
            @Param("tipos") List<TipoTransacao> tipos
    );


    @Query("""
                select new br.com.gestao.financeira.service.transacao.dto.DadosResumoMensal(
                    year(t.dataTransacao),
                    month(t.dataTransacao),
                    sum(t.valor)
                )
                from Transacao t
                where t.ativo = true
                  and t.usuarioId = :usuarioId
                  and t.tipoTransacao in :tipos
                group by year(t.dataTransacao), month(t.dataTransacao)
                order by year(t.dataTransacao), month(t.dataTransacao)
            """)
    List<DadosResumoMensal> totalPorMes(@Param("usuarioId") Long usuarioId,
                                        @Param("tipos") List<TipoTransacao> tipos
    );

}