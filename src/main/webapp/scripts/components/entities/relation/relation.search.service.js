'use strict';

angular.module('relacionesApp')
    .factory('RelationSearch', function ($resource) {
        return $resource('api/_search/relations/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
