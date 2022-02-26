import React from "react";
import { useBackend, useBackendMutation } from "main/utils/useBackend";

import BasicLayout from "main/layouts/BasicLayout/BasicLayout";
import EarthquakesTable from "main/components/Earthquakes/EarthquakesTable";
import { useCurrentUser } from "main/utils/currentUser";
import { Button } from "react-bootstrap";
import { toast } from "react-toastify";
import { Navigate } from "react-router-dom";

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

  const objectToAxiosParams = (earthquake) => ({
    url: "/api/earthquakes/purge",
    method: "POST",
    params: {
      distance: earthquake.dist,
      minMag: earthquake.minMag,
    },
  });

  const tstId = React.useRef(null);

  const onSuccess = () => {};

  const mutation = useBackendMutation(
    objectToAxiosParams,
    { onSuccess },
    // Stryker disable next-line all : hard to set up test for caching
    ["/api/earthquakes/all"]
  );

  const { isSuccess } = mutation;

  const onSubmit = async (data) => {
    mutation.mutate(data);
  };

  if (isSuccess) {
    tstId.current = toast("Earthquakes Purged", {
      toastId: tstId.current,
    });
  }

  return (
    <BasicLayout>
      <div className="pt-2">
        <h1>List Earthquakes</h1>
        <EarthquakesTable Features={ListProperties} currentUser={currentUser} />
        <Button
          type="submit"
          onClick={onSubmit}
          data-testid="EarthquakesList-purge"
        >
          Purge
        </Button>
      </div>
    </BasicLayout>
  );
}
