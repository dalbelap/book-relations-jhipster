/* globals $ */
'use strict';

angular.module('relacionesApp')
    .directive('relacionesAppPagination', function() {
        return {
            templateUrl: 'scripts/components/form/pagination.html'
        };
    });
