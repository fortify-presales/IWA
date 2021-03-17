module.exports = {
    name: 'subscribe-newsletter',
    props: {},
    data: function () {
        return {
            formError: false,
            userEmail: null,
            feedbackMessage: null,
            serverError: false
        };
    },
    async created() {
    },
    methods: {
        async registerUser(e) {

            e.preventDefault();
            e.stopPropagation();

            if (!this.userEmail) {
                this.feedbackMessage = "Please enter your email address to subscribe.";
                this.formError = true;
            } else if (!this.validEmail(this.userEmail)) {
                this.feedbackMessage = "Please provide a valid email address to subscribe";
                this.formError = true;
            } else {
                this.formError = false;
            }

            if (!this.formError) {
                let subscribeRequest = {
                    'firstName': '',
                    'lastName': '',
                    'email': this.userEmail,
                }
                console.log("Subscribing user: " + JSON.stringify(subscribeRequest))
                let axiosConfig = {
                    headers: {
                        'Content-Type': 'application/json;charset=UTF-8',
                        "Access-Control-Allow-Origin": "*",
                    }
                };
                axios.post("/api/v3/site/subscribeUser", JSON.stringify(subscribeRequest), axiosConfig)
                    .then((response) => {
                        this.feedbackMessage = "Thank you - you are now subscribed to our newsletter."
                    })
                    .catch(error => {
                        this.serverError = true;
                        if (error.response) {
                            console.log(error.response.data);
                            this.feedbackMessage = "Error registering: ";
                            if (error.response.data.body) {
                                this.errors = error.response.data.body.errors
                                this.feedbackMessage += error.response.data.body.errors.toString();
                            }
                            //console.log(error.response.status);
                            //console.log(error.response.headers);
                        } else if (error.request) {
                            console.log(error.request);
                        } else {
                            console.log('Error', error.message)
                        }
                        //console.log(error.config);
                    });
            }

            this.showModal();

        },
        showModal() {
            this.$refs['confirmation-modal'].show();
        },
        hideModal() {
            this.$refs['confirmation-modal'].hide()
        },
        validEmail: function (email) {
            const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
            return re.test(email);
        },
    },
    computed: {},
    watch: {},
    mounted() {

    },
};
