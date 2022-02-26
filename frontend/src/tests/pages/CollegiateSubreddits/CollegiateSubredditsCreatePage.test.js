import { render, waitFor, fireEvent } from "@testing-library/react";
import CollegiateSubredditsCreatePage from "main/pages/CollegiateSubreddits/CollegiateSubredditsCreatePage";
import { QueryClient, QueryClientProvider } from "react-query";
import { MemoryRouter } from "react-router-dom";

import { apiCurrentUserFixtures } from "fixtures/currentUserFixtures";
import { systemInfoFixtures } from "fixtures/systemInfoFixtures";
import axios from "axios";
import AxiosMockAdapter from "axios-mock-adapter";

const mockToast = jest.fn();
jest.mock('react-toastify', () => {
    const originalModule = jest.requireActual('react-toastify');
    return {
        __esModule: true,
        ...originalModule,
        toast: (x) => mockToast(x)
    };
});

const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => {
    const originalModule = jest.requireActual('react-router-dom');
    return {
        __esModule: true,
        ...originalModule,
        Navigate: (x) => { mockNavigate(x); return null; }
    };
});

describe("CollegiateSubredditsCreatePage tests", () => {

    const axiosMock =new AxiosMockAdapter(axios);

    beforeEach(() => {
        axiosMock.reset();
        axiosMock.resetHistory();
        axiosMock.onGet("/api/currentUser").reply(200, apiCurrentUserFixtures.userOnly);
        axiosMock.onGet("/api/systemInfo").reply(200, systemInfoFixtures.showingNeither);
    });

    test("renders without crashing", () => {
        const queryClient = new QueryClient();
        render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <CollegiateSubredditsCreatePage />
                </MemoryRouter>
            </QueryClientProvider>
        );
    });

    test("when you fill in the form and hit submit, it makes a request to the backend", async () => {

        const queryClient = new QueryClient();
        const ucsbDate = {
            id: 17,
            name: "UCSB",
            location: "Santa Barbara",
            subreddit: "ucsb"
        };

        axiosMock.onPost("/api/collegiateSubreddits/post").reply( 202, ucsbDate );

        const { getByTestId } = render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <CollegiateSubredditsCreatePage />
                </MemoryRouter>
            </QueryClientProvider>
        );

        await waitFor(() => {
            expect(getByTestId("CollegiateSubredditsForm-name")).toBeInTheDocument();
        });

        const nameField = getByTestId("CollegiateSubredditsForm-name");
        const locationField = getByTestId("CollegiateSubredditsForm-location");
        const subredditField = getByTestId("CollegiateSubredditsForm-subreddit");
        const submitButton = getByTestId("CollegiateSubredditsForm-submit");

        fireEvent.change(nameField, { target: { value: 'UCSB' } });
        fireEvent.change(locationField, { target: { value: 'Santa Barbara' } });
        fireEvent.change(subredditField, { target: { value: 'ucsb' } });

        expect(submitButton).toBeInTheDocument();

        fireEvent.click(submitButton);

        await waitFor(() => expect(axiosMock.history.post.length).toBe(1));

        expect(axiosMock.history.post[0].params).toEqual(
            {
            "name": "UCSB",
            "location": "Santa Barbara",
            "subreddit": "ucsb"
        });

        expect(mockToast).toBeCalledWith("New CollegiateSubreddit Created - id: 17 name: UCSB");
        expect(mockNavigate).toBeCalledWith({ "to": "/collegiatesubreddits/list" });
    });


});


