
jQuery(document).ready(function($) {

    "use strict";

    var searchShow = function() {
        // alert();
        var searchWrap = $('.search-wrap');
        $('.js-search-open').on('click', function(e) {
            e.preventDefault();
            searchWrap.addClass('active');
            setTimeout(function() {
                searchWrap.find('.form-control').focus();
            }, 300);
        });
        $('.js-search-close').on('click', function(e) {
            e.preventDefault();
            searchWrap.removeClass('active');
        })
    };
    searchShow();

    var slider = function() {
        $('.nonloop-block-3').owlCarousel({
            center: false,
            items: 1,
            loop: true,
            smartSpeed: 700,
            stagePadding: 15,
            margin: 20,
            autoplay: true,
            nav: true,
            navText: ['<span class="icon-arrow_back">', '<span class="icon-arrow_forward">'],
            responsive:{
                600:{
                    margin: 20,
                    items: 2
                },
                1000:{
                    margin: 20,
                    items: 3
                },
                1200:{
                    margin: 20,
                    items: 3
                }
            }
        });
    };
    slider();

});

var app = document.getElementById("app");
if (app != null) {
    new Vue({
        el: '#app',
        components: {
            'unread-message-count': httpVueLoader('/js/components/UnreadMessageCount.vue'),
            'shopping-cart-add': httpVueLoader('/js/components/CartAdd.vue'),
            'shopping-cart-count': httpVueLoader('/js/components/CartCount.vue'),
            'shopping-cart-summary': httpVueLoader('/js/components/CartSummary.vue'),
            'shopping-cart': httpVueLoader('/js/components/ShoppingCart.vue'),
            'order-summary': httpVueLoader('/js/components/OrderSummary.vue')
        }
    })
}
