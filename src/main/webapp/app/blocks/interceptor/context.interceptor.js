(function() {
    'use strict';

    angular
        .module('jhmdsApp')
        .factory('contextInterceptor', contextInterceptor);

    contextInterceptor.$inject = ['JhiContextService'];

    function contextInterceptor (JhiContextService) {
        var service = {
            request: context
        };

        return service;

        function context ($config) {
            $config.headers['Context'] = JhiContextService.getCurrentContext();
            return $config;
        }

    }
})();
