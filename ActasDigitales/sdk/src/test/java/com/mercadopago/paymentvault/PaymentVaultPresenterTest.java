package com.mercadopago.paymentvault;

import com.mercadopago.callbacks.OnSelectedCallback;
import com.mercadopago.constants.PaymentTypes;
import com.mercadopago.constants.Sites;
import com.mercadopago.exceptions.MercadoPagoError;
import com.mercadopago.mocks.PaymentMethodSearchs;
import com.mercadopago.model.ApiException;
import com.mercadopago.model.Card;
import com.mercadopago.model.CustomSearchItem;
import com.mercadopago.model.Discount;
import com.mercadopago.model.Payer;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.model.PaymentMethodSearch;
import com.mercadopago.model.PaymentMethodSearchItem;
import com.mercadopago.model.Site;
import com.mercadopago.mvp.OnResourcesRetrievedCallback;
import com.mercadopago.preferences.FlowPreference;
import com.mercadopago.preferences.PaymentPreference;
import com.mercadopago.presenters.PaymentVaultPresenter;
import com.mercadopago.providers.PaymentVaultProvider;
import com.mercadopago.views.PaymentVaultView;

import junit.framework.Assert;

import com.mercadopago.utils.Discounts;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class PaymentVaultPresenterTest {

    @Test
    public void ifSiteNotSetShowInvalidSiteError() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodWithoutCustomOptionsMLA();

        provider.setResponse(paymentMethodSearch);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);

        presenter.initialize(true);

        assertEquals(MockedProvider.INVALID_SITE, mockedView.errorShown.getMessage());
    }

    @Test
    public void ifInvalidCurrencySetShowInvalidSiteError() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodWithoutCustomOptionsMLA();
        provider.setResponse(paymentMethodSearch);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setSite(new Site("invalid_id", "invalid_currency"));
        presenter.setAmount(BigDecimal.TEN);

        presenter.initialize(true);

        assertEquals(MockedProvider.INVALID_SITE, mockedView.errorShown.getMessage());
    }

    @Test
    public void ifAmountNotSetShowInvalidAmountError() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodWithoutCustomOptionsMLA();
        provider.setResponse(paymentMethodSearch);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize(true);

        assertEquals(MockedProvider.INVALID_AMOUNT, mockedView.errorShown.getMessage());
    }

    @Test
    public void ifInvalidAmountSetShowInvalidAmountError() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodWithoutCustomOptionsMLA();
        provider.setResponse(paymentMethodSearch);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setSite(Sites.ARGENTINA);
        presenter.setAmount(BigDecimal.TEN.negate());

        presenter.initialize(true);

        assertEquals(MockedProvider.INVALID_AMOUNT, mockedView.errorShown.getMessage());
    }

    @Test
    public void ifNoPaymentMethodsAvailableThenShowError() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = new PaymentMethodSearch();
        provider.setResponse(paymentMethodSearch);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setSite(Sites.ARGENTINA);
        presenter.setAmount(BigDecimal.TEN);

        presenter.initialize(true);

        assertTrue(mockedView.errorShown.getMessage().equals(MockedProvider.EMPTY_PAYMENT_METHODS));
    }

    @Test
    public void ifPaymentMethodSearchHasItemsShowThem() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodWithoutCustomOptionsMLA();
        provider.setResponse(paymentMethodSearch);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize(true);

        assertEquals(paymentMethodSearch.getGroups(), mockedView.searchItemsShown);
    }

    @Test
    public void ifPaymentMethodSearchHasPayerCustomOptionsShowThem() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();
        provider.setResponse(paymentMethodSearch);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize(true);

        assertEquals(paymentMethodSearch.getCustomSearchItems(), mockedView.customOptionsShown);
    }

    @Test
    public void whenItemWithChildrenSelectedThenShowChildren() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();
        provider.setResponse(paymentMethodSearch);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize(true);
        mockedView.simulateItemSelection(1);

        assertEquals(paymentMethodSearch.getGroups().get(1).getChildren(), mockedView.searchItemsShown);
    }

    @Test
    public void whenDiscountsItemSelectedThenStartDiscountFlow() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();
        provider.setResponse(paymentMethodSearch);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize(true);
        presenter.onDiscountOptionSelected();

        assertTrue(mockedView.discountsFlowStarted);
    }

    //Automatic selections

    @Test
    public void ifOnlyUniqueSearchItemAvailableRestartWithItSelected() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodSearchWithOnlyTicketMLA();
        provider.setResponse(paymentMethodSearch);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize(true);

        assertEquals(paymentMethodSearch.getGroups().get(0), mockedView.itemShown);
    }

    @Test
    public void ifOnlyCardPaymentTypeAvailableStartCardFlow() {

        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodSearchWithOnlyCreditCardMLA();
        provider.setResponse(paymentMethodSearch);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        BigDecimal amount = BigDecimal.TEN;
        presenter.setAmount(amount);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize(true);

        assertTrue(mockedView.cardFlowStarted);
        assertEquals(paymentMethodSearch.getGroups().get(0).getId(), mockedView.selectedPaymentType);
        assertEquals(amount, mockedView.amountSentToCardFlow);
    }

    @Test
    public void ifOnlyCardPaymentTypeAvailableAndCardAvailableDoNotSelectAutomatically() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodSearchWithOnlyCreditCardAndOneCardMLA();
        provider.setResponse(paymentMethodSearch);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        BigDecimal amount = BigDecimal.TEN;
        presenter.setAmount(amount);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize(true);

        assertTrue(mockedView.customOptionsShown != null);
        assertFalse(mockedView.cardFlowStarted);
        assertFalse(mockedView.isItemShown);
    }

    @Test
    public void ifOnlyCardPaymentTypeAvailableButAutomaticSelectionDisabledThenDoNotSelectAutomatically() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodSearchWithOnlyCreditCardMLA();
        provider.setResponse(paymentMethodSearch);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        BigDecimal amount = BigDecimal.TEN;
        presenter.setAmount(amount);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize(false);

        assertFalse(mockedView.cardFlowStarted);
    }

    @Test
    public void ifOnlyCardPaymentTypeAvailableAndAccountMoneyAvailableDoNotSelectAutomatically() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodSearchWithOnlyCreditCardAndAccountMoneyMLA();
        provider.setResponse(paymentMethodSearch);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        BigDecimal amount = BigDecimal.TEN;
        presenter.setAmount(amount);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize(true);

        assertTrue(mockedView.customOptionsShown != null);
        assertFalse(mockedView.cardFlowStarted);
        assertFalse(mockedView.isItemShown);
    }

    @Test
    public void ifOnlyOffPaymentTypeAvailableAndAccountMoneyAvailableDoNotSelectAutomatically() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodSearchWithOnlyOneOffTypeAndAccountMoneyMLA();
        provider.setResponse(paymentMethodSearch);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        BigDecimal amount = BigDecimal.TEN;
        presenter.setAmount(amount);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize(true);

        assertTrue(mockedView.customOptionsShown != null);
        assertFalse(mockedView.cardFlowStarted);
        assertFalse(mockedView.isItemShown);
    }

    @Test
    public void ifOnlyAccountMoneySelectItAutomatically() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodSearchWithOnlyAccountMoneyMLA();
        provider.setResponse(paymentMethodSearch);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        BigDecimal amount = BigDecimal.TEN;
        presenter.setAmount(amount);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize(true);

        assertEquals(paymentMethodSearch.getCustomSearchItems().get(0).getPaymentMethodId(), mockedView.selectedPaymentMethod.getId());
    }

    //User selections

    @Test
    public void ifItemSelectedShowItsChildren() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();
        provider.setResponse(paymentMethodSearch);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize(true);

        mockedView.simulateItemSelection(1);

        assertEquals(paymentMethodSearch.getGroups().get(1).getChildren(), mockedView.searchItemsShown);
        assertEquals(paymentMethodSearch.getGroups().get(1), mockedView.itemShown);
    }

    @Test
    public void ifCardPaymentTypeSelectedStartCardFlow() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();
        provider.setResponse(paymentMethodSearch);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize(true);

        mockedView.simulateItemSelection(0);

        assertEquals(paymentMethodSearch.getGroups().get(0).getId(), mockedView.selectedPaymentType);
        assertTrue(mockedView.cardFlowStarted);
    }

    @Test
    public void ifSavedCardSelectedStartSavedCardFlow() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();
        provider.setResponse(paymentMethodSearch);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize(true);

        mockedView.simulateCustomItemSelection(1);

        assertTrue(mockedView.savedCardFlowStarted);
        assertTrue(mockedView.savedCardSelected.equals(paymentMethodSearch.getCards().get(0)));
    }

    @Test
    public void ifAccountMoneySelectedSelectIt() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();
        provider.setResponse(paymentMethodSearch);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize(true);

        mockedView.simulateCustomItemSelection(0);

        assertEquals(paymentMethodSearch.getCustomSearchItems().get(0).getPaymentMethodId(), mockedView.selectedPaymentMethod.getId());
    }

    //Payment Preference tests
    @Test
    public void ifAllPaymentTypesExcludedShowError() {
        PaymentPreference paymentPreference = new PaymentPreference();
        paymentPreference.setExcludedPaymentTypeIds(PaymentTypes.getAllPaymentTypes());

        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodWithoutCustomOptionsMLA();
        provider.setResponse(paymentMethodSearch);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setSite(Sites.ARGENTINA);
        presenter.setAmount(BigDecimal.TEN);
        presenter.setPaymentPreference(paymentPreference);

        presenter.initialize(true);

        assertEquals(MockedProvider.ALL_TYPES_EXCLUDED, mockedView.errorShown.getMessage());
    }

    @Test
    public void ifInvalidDefaultInstallmentsShowError() {
        PaymentPreference paymentPreference = new PaymentPreference();
        paymentPreference.setDefaultInstallments(BigDecimal.ONE.negate().intValue());

        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodWithoutCustomOptionsMLA();
        provider.setResponse(paymentMethodSearch);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setSite(Sites.ARGENTINA);
        presenter.setAmount(BigDecimal.TEN);
        presenter.setPaymentPreference(paymentPreference);

        presenter.initialize(true);

        assertEquals(MockedProvider.INVALID_DEFAULT_INSTALLMENTS, mockedView.errorShown.getMessage());
    }

    @Test
    public void ifInvalidMaxInstallmentsShowError() {
        PaymentPreference paymentPreference = new PaymentPreference();
        paymentPreference.setMaxAcceptedInstallments(BigDecimal.ONE.negate().intValue());

        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodWithoutCustomOptionsMLA();
        provider.setResponse(paymentMethodSearch);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setSite(Sites.ARGENTINA);
        presenter.setAmount(BigDecimal.TEN);
        presenter.setPaymentPreference(paymentPreference);

        presenter.initialize(true);

        assertEquals(MockedProvider.INVALID_MAX_INSTALLMENTS, mockedView.errorShown.getMessage());
    }

    @Test
    public void ifMaxSavedCardNotSetDoNotLimitCardsShown() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();
        provider.setResponse(paymentMethodSearch);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize(true);

        Assert.assertEquals(paymentMethodSearch.getCustomSearchItems().size(), mockedView.customOptionsShown.size());
    }

    @Test
    public void ifMaxSavedCardLimitCardsShown() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();
        provider.setResponse(paymentMethodSearch);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);
        presenter.setMaxSavedCards(1);

        presenter.initialize(true);

        //Account money + 1 card
        Assert.assertEquals(2, mockedView.customOptionsShown.size());
    }

    //Discounts
    @Test
    public void ifDiscountsAreNotEnabledNotShowDiscountRow() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();
        provider.setResponse(paymentMethodSearch);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);
        presenter.setDiscountEnabled(false);

        presenter.initialize(true);

        mockedView.simulateItemSelection(0);

        assertTrue(mockedView.showedDiscountRow);
        assertTrue(presenter.getDiscount() == null);
    }

    @Test
    public void ifDiscountsAreEnabledGetDirectDiscount() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();
        provider.setResponse(paymentMethodSearch);

        Discount discount = Discounts.getDiscountWithAmountOffMLA();
        provider.setDiscountResponse(discount);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize(true);
    }

    @Test
    public void ifHasNotDirectDiscountsShowDiscountRow() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();
        provider.setResponse(paymentMethodSearch);

        ApiException apiException = Discounts.getDoNotFindCampaignApiException();
        MercadoPagoError mpException = new MercadoPagoError(apiException, "");
        provider.setDiscountResponse(mpException);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize(true);

        mockedView.simulateItemSelection(0);

        assertTrue(provider.failedResponse.getApiException().getError().equals(provider.CAMPAIGN_DOES_NOT_MATCH_ERROR));
        assertTrue(mockedView.showedDiscountRow);
    }

    @Test
    public void ifIsDirectDiscountNotEnabledNotGetDirectDiscount() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();
        provider.setResponse(paymentMethodSearch);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);
        presenter.setDirectDiscountEnabled(false);

        presenter.initialize(true);

        mockedView.simulateItemSelection(0);

        assertTrue(mockedView.showedDiscountRow);
    }

    @Test
    public void ifHasNotDirectDiscountSetFalseDirectDiscountEnabled() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();
        provider.setResponse(paymentMethodSearch);

        ApiException apiException = Discounts.getDoNotFindCampaignApiException();
        MercadoPagoError mpException = new MercadoPagoError(apiException, "");
        provider.setDiscountResponse(mpException);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize(true);

        mockedView.simulateItemSelection(0);

        assertFalse(presenter.getDirectDiscountEnabled());
        assertTrue(mockedView.showedDiscountRow);
    }

    @Test
    public void ifResourcesRetrievalFailThenShowError() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        ApiException apiException = new ApiException();
        apiException.setMessage("Mocked failure");
        MercadoPagoError mercadoPagoError = new MercadoPagoError(apiException, "");
        provider.setResponse(mercadoPagoError);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);
        presenter.setMaxSavedCards(1);

        presenter.initialize(true);

        Assert.assertTrue(mockedView.errorShown.getApiException().equals(mercadoPagoError.getApiException()));
    }

    @Test
    public void whenResourcesRetrievalFailedAndRecoverRequestedThenRepeatRetrieval() {
        //Set Up
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        ApiException apiException = new ApiException();
        apiException.setMessage("Mocked failure");
        MercadoPagoError mercadoPagoError = new MercadoPagoError(apiException, "");
        provider.setResponse(mercadoPagoError);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);
        presenter.setMaxSavedCards(1);

        presenter.initialize(true);
        //Presenter gets resources, fails

        provider.setResponse(PaymentMethodSearchs.getCompletePaymentMethodSearchMLA());
        presenter.recoverFromFailure();

        Assert.assertFalse(mockedView.searchItemsShown.isEmpty());
    }

    @Test
    public void whenResourcesRetrievalFailedButNoViewAttachedThenDoNotRepeatRetrieval() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        ApiException apiException = new ApiException();
        apiException.setMessage("Mocked failure");
        MercadoPagoError mercadoPagoError = new MercadoPagoError(apiException, "");
        provider.setResponse(mercadoPagoError);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);
        presenter.setMaxSavedCards(1);

        presenter.initialize(true);

        presenter.detachView();

        provider.setResponse(PaymentMethodSearchs.getCompletePaymentMethodSearchMLA());
        presenter.recoverFromFailure();

        Assert.assertTrue(mockedView.searchItemsShown == null);
    }

    @Test
    public void onDiscountReceivedThenShowIt() {
        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();
        
        provider.setResponse(PaymentMethodSearchs.getCompletePaymentMethodSearchMLA());
        presenter.recoverFromFailure();
        
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);
        presenter.setMaxSavedCards(1);

        presenter.initialize(true);
        
        Discount discount = new Discount();
        discount.setCurrencyId("ARS");
        discount.setId(123L);
        discount.setAmountOff(new BigDecimal("10"));
        discount.setCouponAmount(new BigDecimal("10"));
        presenter.onDiscountReceived(discount);
        
        Assert.assertTrue(mockedView.showedDiscountRow);
    }

    @Test
    public void onDiscountReceivedThenRetrievePaymentMethodsAgain() {
        PaymentVaultPresenter presenter = new PaymentVaultPresenter();

        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch originalPaymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();
        originalPaymentMethodSearch.getGroups().remove(0);

        provider.setResponse(originalPaymentMethodSearch);

        presenter.recoverFromFailure();

        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);
        presenter.setMaxSavedCards(1);

        presenter.initialize(true);

        provider.setResponse(PaymentMethodSearchs.getCompletePaymentMethodSearchMLA());

        Discount discount = new Discount();
        discount.setCurrencyId("ARS");
        discount.setId(123L);
        discount.setAmountOff(new BigDecimal("10"));
        discount.setCouponAmount(new BigDecimal("10"));
        presenter.onDiscountReceived(discount);

        Assert.assertTrue(mockedView.searchItemsShown.size() != originalPaymentMethodSearch.getGroups().size());
    }

    @Test
    public void ifPaymentMethodSearchSetAndHasItemsThenShowThem() {
        PaymentVaultPresenter presenter = new PaymentVaultPresenter();

        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodWithoutCustomOptionsMLA();

        presenter.setPaymentMethodSearch(paymentMethodSearch);
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize(true);

        assertEquals(paymentMethodSearch.getGroups(), mockedView.searchItemsShown);
    }

    @Test
    public void ifPaymentMethodSearchItemIsNotCardAndDoesNotHaveChildrenThenStartPaymentMethodsSelection() {
        PaymentVaultPresenter presenter = new PaymentVaultPresenter();

        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();
        paymentMethodSearch.getGroups().get(1).getChildren().removeAll(paymentMethodSearch.getGroups().get(1).getChildren());

        provider.setResponse(paymentMethodSearch);

        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize(true);

        mockedView.simulateItemSelection(1);
        assertTrue(mockedView.paymentMethodSelectionStarted);
    }

    @Test
    public void ifPaymentMethodTypeSelectedThenSelectPaymentMethod() {
        PaymentVaultPresenter presenter = new PaymentVaultPresenter();

        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodSearchWithPaymentMethodOnTop();

        provider.setResponse(paymentMethodSearch);

        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize(true);

        mockedView.simulateItemSelection(1);
        assertTrue(paymentMethodSearch.getGroups().get(1).getId().equals(mockedView.selectedPaymentMethod.getId()));
    }

    @Test
    public void ifShowAllSavedCardsTestThenShowThem() {

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();

        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        // 6 Saved Cards + Account Money
        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodSearchWithSavedCardsMLA();

        provider.setResponse(paymentMethodSearch);

        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);
        // Set show all saved cards
        presenter.setShowAllSavedCardsEnabled(true);
        presenter.setMaxSavedCards(FlowPreference.DEFAULT_MAX_SAVED_CARDS_TO_SHOW);

        presenter.initialize(true);

        assertEquals(mockedView.customOptionsShown.size(), paymentMethodSearch.getCustomSearchItems().size());
    }

    @Test
    public void ifMaxSavedCardsSetThenShowWithLimit() {

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();

        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        // 6 Saved Cards + Account Money
        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodSearchWithSavedCardsMLA();

        provider.setResponse(paymentMethodSearch);

        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);
        presenter.setMaxSavedCards(4);

        presenter.initialize(true);

        // 4 Cards + Account Money
        assertEquals(mockedView.customOptionsShown.size(), 5);
    }

    @Test
    public void ifMaxSavedCardsSetThenShowWithLimitAgain() {

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();

        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        // 6 Saved Cards + Account Money
        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodSearchWithSavedCardsMLA();

        provider.setResponse(paymentMethodSearch);

        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);
        presenter.setMaxSavedCards(1);

        presenter.initialize(true);

        // 1 Card + Account Money
        assertEquals(mockedView.customOptionsShown.size(), 2);
    }

    @Test
    public void ifMaxSavedCardsSetAndShowAllSetThenShowAllSavedCards() {

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();

        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        // 6 Saved Cards + Account Money
        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodSearchWithSavedCardsMLA();

        provider.setResponse(paymentMethodSearch);

        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);
        presenter.setShowAllSavedCardsEnabled(true);
        presenter.setMaxSavedCards(4);

        presenter.initialize(true);

        assertEquals(mockedView.customOptionsShown.size(), paymentMethodSearch.getCustomSearchItems().size());
    }

    @Test
    public void ifMaxSavedCardsSetMoreThanActualAmountOfCardsThenShowAll() {

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();

        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        // 6 Saved Cards + Account Money
        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodSearchWithSavedCardsMLA();

        provider.setResponse(paymentMethodSearch);

        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);
        // More cards than we have
        presenter.setMaxSavedCards(8);

        presenter.initialize(true);

        // Show every card we have
        assertEquals(mockedView.customOptionsShown.size(), paymentMethodSearch.getCustomSearchItems().size());
    }

    private class MockedProvider implements PaymentVaultProvider {

        private static final String INVALID_SITE = "invalid site";
        private static final String INVALID_AMOUNT = "invalid amount";
        private static final String ALL_TYPES_EXCLUDED = "all types excluded";
        private static final String INVALID_DEFAULT_INSTALLMENTS = "invalid default installments";
        private static final String INVALID_MAX_INSTALLMENTS = "invalid max installments";
        private static final String STANDARD_ERROR_MESSAGE = "standard error";
        private static final String EMPTY_PAYMENT_METHODS = "empty payment methods";
        private static final String CAMPAIGN_DOES_NOT_MATCH_ERROR = "campaign-doesnt-match";

        private boolean shouldFail;
        private boolean shouldDiscountFail;
        private PaymentMethodSearch successfulResponse;
        private Discount successfulDiscountResponse;
        private MercadoPagoError failedResponse;

        public void setResponse(PaymentMethodSearch paymentMethodSearch) {
            shouldFail = false;
            successfulResponse = paymentMethodSearch;
        }

        public void setResponse(MercadoPagoError exception) {
            shouldFail = true;
            failedResponse = exception;
        }

        public void setDiscountResponse(Discount discount) {
            shouldDiscountFail = false;
            successfulDiscountResponse = discount;
        }

        public void setDiscountResponse(MercadoPagoError exception) {
            shouldDiscountFail = true;
            failedResponse = exception;
        }

        @Override
        public String getTitle() {
            return "¿Cómo quieres pagar?";
        }

        @Override
        public void getPaymentMethodSearch(BigDecimal amount, PaymentPreference paymentPreference, Payer payer, Site site, OnResourcesRetrievedCallback<PaymentMethodSearch> onResourcesRetrievedCallback) {
            if (shouldFail) {
                onResourcesRetrievedCallback.onFailure(failedResponse);
            } else {
                onResourcesRetrievedCallback.onSuccess(successfulResponse);
            }
        }

        @Override
        public void getDirectDiscount(String amount, String payerEmail, OnResourcesRetrievedCallback<Discount> onResourcesRetrievedCallback) {
            if (shouldDiscountFail) {
                onResourcesRetrievedCallback.onFailure(failedResponse);
            } else {
                onResourcesRetrievedCallback.onSuccess(successfulDiscountResponse);
            }
        }

        @Override
        public String getInvalidSiteConfigurationErrorMessage() {
            return INVALID_SITE;
        }

        @Override
        public String getInvalidAmountErrorMessage() {
            return INVALID_AMOUNT;
        }

        @Override
        public String getAllPaymentTypesExcludedErrorMessage() {
            return ALL_TYPES_EXCLUDED;
        }

        @Override
        public String getInvalidDefaultInstallmentsErrorMessage() {
            return INVALID_DEFAULT_INSTALLMENTS;
        }

        @Override
        public String getInvalidMaxInstallmentsErrorMessage() {
            return INVALID_MAX_INSTALLMENTS;
        }

        @Override
        public String getStandardErrorMessage() {
            return STANDARD_ERROR_MESSAGE;
        }

        @Override
        public String getEmptyPaymentMethodsErrorMessage() {
            return EMPTY_PAYMENT_METHODS;
        }
    }

    private class MockedView implements PaymentVaultView {

        private List<PaymentMethodSearchItem> searchItemsShown;
        private MercadoPagoError errorShown;
        private List<CustomSearchItem> customOptionsShown;
        private PaymentMethodSearchItem itemShown;
        private boolean cardFlowStarted = false;
        private BigDecimal amountSentToCardFlow;
        private boolean isItemShown;
        private PaymentMethod selectedPaymentMethod;
        private OnSelectedCallback<PaymentMethodSearchItem> itemSelectionCallback;
        private OnSelectedCallback<CustomSearchItem> customItemSelectionCallback;
        private String title;
        private boolean savedCardFlowStarted;
        private Card savedCardSelected;
        private String selectedPaymentType;
        private Boolean showedDiscountRow;
        public boolean discountsFlowStarted = false;
        public boolean paymentMethodSelectionStarted = false;

        @Override
        public void startSavedCardFlow(Card card, BigDecimal transactionAmount) {
            this.savedCardFlowStarted = true;
            this.savedCardSelected = card;
        }

        @Override
        public void showSelectedItem(PaymentMethodSearchItem item) {
            this.itemShown = item;
            this.isItemShown = true;
            this.searchItemsShown = item.getChildren();
        }

        @Override
        public void showProgress() {
            //Not yet tested
        }

        @Override
        public void hideProgress() {
            //Not yet tested
        }

        @Override
        public void showCustomOptions(List<CustomSearchItem> customSearchItems, OnSelectedCallback<CustomSearchItem> customSearchItemOnSelectedCallback) {
            this.customOptionsShown = customSearchItems;
            this.customItemSelectionCallback = customSearchItemOnSelectedCallback;
        }

        @Override
        public void showSearchItems(List<PaymentMethodSearchItem> searchItems, OnSelectedCallback<PaymentMethodSearchItem> paymentMethodSearchItemSelectionCallback) {
            this.searchItemsShown = searchItems;
            this.itemSelectionCallback = paymentMethodSearchItemSelectionCallback;
        }

        @Override
        public void showError(MercadoPagoError mpException, String requestOrigin) {
            errorShown = mpException;
        }

        @Override
        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public void startCardFlow(String paymentType, BigDecimal transactionAmount, Boolean automaticallySelection) {
            cardFlowStarted = true;
            amountSentToCardFlow = transactionAmount;
            selectedPaymentType = paymentType;
        }

        @Override
        public void startPaymentMethodsSelection() {
            paymentMethodSelectionStarted = true;
        }

        @Override
        public void selectPaymentMethod(PaymentMethod selectedPaymentMethod) {
            this.selectedPaymentMethod = selectedPaymentMethod;
        }

        @Override
        public void showDiscount(BigDecimal transactionAmount) {
            this.showedDiscountRow = true;
        }

        @Override
        public void startDiscountFlow(BigDecimal transactionAmount) {
            discountsFlowStarted = true;
        }

        @Override
        public void cleanPaymentMethodOptions() {
            //Not yet tested
        }

        @Override
        public void trackChildrenScreen() {

        }

        @Override
        public void initializeMPTracker() {

        }

        @Override
        public void trackInitialScreen() {

        }

        public void simulateItemSelection(int index) {
            itemSelectionCallback.onSelected(searchItemsShown.get(index));
        }

        public void simulateCustomItemSelection(int index) {
            customItemSelectionCallback.onSelected(customOptionsShown.get(index));
        }
    }

}
