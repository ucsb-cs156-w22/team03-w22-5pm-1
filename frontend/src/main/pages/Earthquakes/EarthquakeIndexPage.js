import React from "react";
import { useBackend } from "main/utils/useBackend";

import BasicLayout from "main/layouts/BasicLayout/BasicLayout";
import EarthquakesTable from "main/components/Earthquakes/EarthquakesTable";
import { useCurrentUser } from "main/utils/currentUser";

export default function EarthquakeIndexPage() {
  const currentUser = useCurrentUser();

  const {
    data: earthquakes,
    error: _error,
    status: _status,
  } = useBackend(
    // Stryker disable next-line all : don't test internal caching of React Query
    ["/api/earthquakes/all"],
    { method: "GET", url: "/api/earthquakes/all" },
    []
  );

  const ListProperties = [];
  earthquakes.forEach(function (prop) {
    ListProperties.push(prop.properties);
  });

  return (
    <BasicLayout>
      <div className="pt-2">
        <h1>List Earthquakes</h1>
        <EarthquakesTable Features={ListProperties} currentUser={currentUser} />
      </div>
    </BasicLayout>
  );
}
