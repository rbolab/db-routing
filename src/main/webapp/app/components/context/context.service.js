(function () {
    'use strict';

    angular
        .module('jhmdsApp')
        .factory('JhiContextService', JhiContextService);

    JhiContextService.$inject = ['$sessionStorage'];

    function JhiContextService ($sessionStorage) {
        var service = {
            getCurrentContext: getCurrentContext
        };

        return service;

        function getCurrentContext () {
            return $sessionStorage.context;
        }
    }
})();
