
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
            'shopping-cart': httpVueLoader('/js/components/ShoppingCart.vue')
        }
    })
}
