'use strict';

angular.module('bidarApp')
    .factory('RelationSearch', function ($resource) {
        return $resource('api/_search/relations/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
