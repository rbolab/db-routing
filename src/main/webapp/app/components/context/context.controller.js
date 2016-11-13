(function() {
    'use strict';

    angular
        .module('jhmdsApp')
        .controller('JhiContextController', JhiContextController);

    JhiContextController.$inject = ['$sessionStorage', '$state'];

    function JhiContextController ($sessionStorage, $state) {
        var vm = this;

        vm.changeContext = changeContext;
        vm.contexts = ['online', 'draft'];

        function changeContext (contextKey) {
            $sessionStorage.context = contextKey;
            $state.reload();
        }
    }
})();
