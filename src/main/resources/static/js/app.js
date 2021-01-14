
var app = document.getElementById("app");
if (app != null) {
    new Vue({
        el: '#app',
        components: {
            'unread-message-count': httpVueLoader('/js/components/UnreadMessageCount.vue')
        }
    })
}
