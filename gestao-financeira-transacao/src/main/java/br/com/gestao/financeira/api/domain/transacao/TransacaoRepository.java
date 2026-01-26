package br.com.gestao.financeira.api.domain.transacao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    Page<Transacao> findAllByAtivoTrue(Pageable paginacao);

}