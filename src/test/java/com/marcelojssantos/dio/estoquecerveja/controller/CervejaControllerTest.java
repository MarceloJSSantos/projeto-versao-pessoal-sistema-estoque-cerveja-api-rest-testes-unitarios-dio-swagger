package com.marcelojssantos.dio.estoquecerveja.controller;

import com.marcelojssantos.dio.estoquecerveja.builder.CervejaDTOBuilder;
import com.marcelojssantos.dio.estoquecerveja.dto.CervejaDTO;
import com.marcelojssantos.dio.estoquecerveja.dto.QuantidadeDTO;
import com.marcelojssantos.dio.estoquecerveja.exception.CervejaNaoEncontradaException;
import com.marcelojssantos.dio.estoquecerveja.exception.EstoqueCervejaMaxQuantExcedidoException;
import com.marcelojssantos.dio.estoquecerveja.exception.EstoqueCervejaMaxQuantMenorZeroException;
import com.marcelojssantos.dio.estoquecerveja.service.CervejaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;

import static com.marcelojssantos.dio.estoquecerveja.utils.utilitarioConverteJson.comoJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CervejaControllerTest {

    private static final String CERVEJA_API_URL_PATH = "/api/v1/cervejas";
    private static final long CERVEJA_VALIDA_ID = 1L;
    private static final long CERVEJA_INVALIDA_ID = 2L;
    private static final String CERVEJA_API_URL_SUBPATH_INCREMENTA = "/incrementa";
    //private static final String CERVEJA_API_URL_SUBPATH_DECREMENTA = "/decrementa";

    @InjectMocks
    private CervejaController cervejaController;

    @Mock
    private CervejaService cervejaService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cervejaController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void quandoPOSTInsereCervejaEChamadoCervejaEInseridaEntaoStatusCreatedERetornado() throws Exception {
        //given
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();

        // when
        when(cervejaService.insert(cervejaDTO)).thenReturn(cervejaDTO);

        //then
        mockMvc.perform(post(CERVEJA_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(comoJsonString(cervejaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", is(cervejaDTO.getNome())))
                .andExpect(jsonPath("$.marca", is(cervejaDTO.getMarca())))
                .andExpect(jsonPath("$.tipo", is(cervejaDTO.getTipo().toString())));
    }

    @Test
    void quandoPOSTInsereCervejaEChamadoSemCampoRequeridoEntaoExceptionERetornado() throws Exception {
        //given
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
        cervejaDTO.setMarca(null);

        //then
        mockMvc.perform(post(CERVEJA_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(comoJsonString(cervejaDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void quandoGETEncontraPorNomeEChamadoComNomeValidoEntaoStatusOKERetornado() throws Exception {
        //given
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();

        //when
        when(cervejaService.findByNome(cervejaDTO.getNome())).thenReturn(cervejaDTO);

        //then
        mockMvc.perform(get(CERVEJA_API_URL_PATH + "/" + cervejaDTO.getNome())
                .contentType(MediaType.APPLICATION_JSON)
                .content(comoJsonString(cervejaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is(cervejaDTO.getNome())))
                .andExpect(jsonPath("$.marca", is(cervejaDTO.getMarca())))
                .andExpect(jsonPath("$.tipo", is(cervejaDTO.getTipo().toString())));
    }

    @Test
    void quandoGETEncontraPorNomeEChamadoComNomeNaoEncontradoEntaoStatusNotFoundRetornado() throws Exception {
        //given
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();

        //when
        when(cervejaService.findByNome(cervejaDTO.getNome()))
                .thenThrow(CervejaNaoEncontradaException.class);

        //then
        mockMvc.perform(get(CERVEJA_API_URL_PATH + "/" + cervejaDTO.getNome())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void quandoGETListaCervejasEChamadoERetornaCervejasEntaoStatusOkERetornado() throws Exception {
        //given
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();

        //when
        when(cervejaService.listAll()).thenReturn(Collections.singletonList(cervejaDTO));

        //then
        mockMvc.perform(get(CERVEJA_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(comoJsonString(cervejaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome", is(cervejaDTO.getNome())))
                .andExpect(jsonPath("$[0].marca", is(cervejaDTO.getMarca())))
                .andExpect(jsonPath("$[0].tipo", is(cervejaDTO.getTipo().toString())));
    }

    @Test
    void quandoGETListaCervejasEChamadoERetornaListaVaziaEntaoStatusOkERetornado() throws Exception {
        //when
        when(cervejaService.listAll()).thenReturn(Collections.emptyList());

        //then
        mockMvc.perform(get(CERVEJA_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(comoJsonString(Collections.emptyList())))
                .andExpect(status().isOk());
    }

    @Test
    void quandoDELETEDeletaPorIdEChamadoComIdValidoEntaoStatusNoContentERetornado() throws Exception {
        //given
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();

        //when
        doNothing().when(cervejaService).deleteById(cervejaDTO.getId());

        //then
        mockMvc.perform(delete(CERVEJA_API_URL_PATH + "/" + CERVEJA_VALIDA_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void quandoDELETEDeletaPorIdEChamadoComIdNaoEncontradoEntaoStatusNotFoundERetornado() throws Exception {
        //when
        doThrow(CervejaNaoEncontradaException.class).when(cervejaService)
                .deleteById(CERVEJA_INVALIDA_ID);

        //then
        mockMvc.perform(delete(CERVEJA_API_URL_PATH + "/" + CERVEJA_INVALIDA_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void quandoPATCHAlteraQuantidadeEChamadoEntaoStatusOkERetornado() throws Exception {
        QuantidadeDTO quantidadeDTO = QuantidadeDTO.builder()
                .quantidade(10)
                .build();

        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
        cervejaDTO.setQuantidade(cervejaDTO.getQuantidade() + quantidadeDTO.getQuantidade());

        when(cervejaService.changeStock(CERVEJA_VALIDA_ID, quantidadeDTO.getQuantidade()))
                .thenReturn(cervejaDTO);

        mockMvc.perform(patch(CERVEJA_API_URL_PATH + "/" + CERVEJA_VALIDA_ID
                + CERVEJA_API_URL_SUBPATH_INCREMENTA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(comoJsonString(quantidadeDTO))).andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is(cervejaDTO.getNome())))
                .andExpect(jsonPath("$.marca", is(cervejaDTO.getMarca())))
                .andExpect(jsonPath("$.tipo", is(cervejaDTO.getTipo().toString())))
                .andExpect(jsonPath("$.quantidade", is(cervejaDTO.getQuantidade())));
    }

    @Test
    void quandoPATCHAlteraQuantidadeEChamadoESomaEMaiorMaxQuantEntaoExceptionERetornada()
            throws Exception {
        QuantidadeDTO quantidadeDTO = QuantidadeDTO.builder()
                .quantidade(30)
                .build();

        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
        cervejaDTO.setQuantidade(cervejaDTO.getQuantidade() + quantidadeDTO.getQuantidade());

        when(cervejaService.changeStock(CERVEJA_VALIDA_ID, quantidadeDTO.getQuantidade()))
                .thenThrow(EstoqueCervejaMaxQuantExcedidoException.class);

        mockMvc.perform(patch(CERVEJA_API_URL_PATH + "/" + CERVEJA_VALIDA_ID
                + CERVEJA_API_URL_SUBPATH_INCREMENTA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(comoJsonString(quantidadeDTO))).andExpect(status().isBadRequest());
    }

    @Test
    void quandoPATCHAlteraQuantidadeEChamadoESomaEMenorQueQuantidadeZeroEntaoExceptionERetornada()
            throws Exception {
        QuantidadeDTO quantidadeDTO = QuantidadeDTO.builder()
                .quantidade(-30)
                .build();

        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
        cervejaDTO.setQuantidade(cervejaDTO.getQuantidade() + quantidadeDTO.getQuantidade());

        when(cervejaService.changeStock(CERVEJA_VALIDA_ID, quantidadeDTO.getQuantidade()))
                .thenThrow(EstoqueCervejaMaxQuantMenorZeroException.class);

        mockMvc.perform(patch(CERVEJA_API_URL_PATH + "/" + CERVEJA_VALIDA_ID
                + CERVEJA_API_URL_SUBPATH_INCREMENTA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(comoJsonString(quantidadeDTO))).andExpect(status().isBadRequest());
    }

    @Test
    void quandoPATCHAlteraQuantidadeEChamadoComIdInvalidoEntaoStatusNotFoundERetornado() throws Exception {
        QuantidadeDTO quantidadeDTO = QuantidadeDTO.builder()
                .quantidade(30)
                .build();

        when(cervejaService.changeStock(CERVEJA_INVALIDA_ID, quantidadeDTO.getQuantidade()))
                .thenThrow(CervejaNaoEncontradaException.class);

        mockMvc.perform(patch(CERVEJA_API_URL_PATH + "/" + CERVEJA_INVALIDA_ID
                + CERVEJA_API_URL_SUBPATH_INCREMENTA)
                .contentType(MediaType.APPLICATION_JSON)
                .content(comoJsonString(quantidadeDTO)))
                .andExpect(status().isNotFound());
    }
}