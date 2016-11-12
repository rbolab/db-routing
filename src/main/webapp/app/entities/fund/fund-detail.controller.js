(function() {
    'use strict';

    angular
        .module('jhmdsApp')
        .controller('FundDetailController', FundDetailController);

    FundDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Fund'];

    function FundDetailController($scope, $rootScope, $stateParams, previousState, entity, Fund) {
        var vm = this;

        vm.fund = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhmdsApp:fundUpdate', function(event, result) {
            vm.fund = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
