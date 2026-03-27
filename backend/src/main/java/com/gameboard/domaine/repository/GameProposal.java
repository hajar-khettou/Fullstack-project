// GameProposalRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gameboard.domain.entity.GameProposal;
import com.gameboard.domain.enums.ProposalStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GameProposalRepository extends JpaRepository<GameProposal, UUID> {

    List<GameProposal> findByStatus(ProposalStatus status);

    List<GameProposal> findByProposedById(UUID userId);

    Optional<GameProposal> findByBggIdAndStatus(String bggId, ProposalStatus status);
}